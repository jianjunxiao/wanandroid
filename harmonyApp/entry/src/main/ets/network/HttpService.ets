import { http } from '@kit.NetworkKit';
import { util } from '@kit.ArkTS';

interface NativeRequest {
  url: string;
  method: string;
  headers: Record<string, string>;
  body: string;
}

interface NativeResponse {
  status: number;
  headers: Record<string, string[]>;
  body: string;
  error?: string;
}

export class HttpService {
  private requests: Map<number, http.HttpRequest> = new Map();
  private base64: util.Base64Helper = new util.Base64Helper();
  private onComplete: (id: number, response: string) => void;

  constructor(onComplete: (id: number, response: string) => void) {
    this.onComplete = onComplete;
  }

  request(id: number, serialized: string): void {
    const request = JSON.parse(serialized) as NativeRequest;
    const client = http.createHttp();
    this.requests.set(id, client);
    const options: http.HttpRequestOptions = {
      method: request.method as http.RequestMethod,
      header: request.headers,
      expectDataType: http.HttpDataType.ARRAY_BUFFER,
      usingCache: false,
      connectTimeout: 10000,
      readTimeout: 10000
    };
    if (request.body.length > 0) options.extraData = this.base64.decodeSync(request.body).buffer as ArrayBuffer;
    client.request(request.url, options).then((result: http.HttpResponse) => {
      const headers: Record<string, string[]> = {};
      const original = result.header as Record<string, string | string[]>;
      Object.keys(original).forEach((key: string) => {
        const value = original[key];
        headers[key] = typeof value === 'string' ? [value] : value;
      });
      const response: NativeResponse = {
        status: result.responseCode,
        headers: headers,
        body: this.base64.encodeToStringSync(new Uint8Array(result.result as ArrayBuffer))
      };
      if (this.requests.has(id)) this.onComplete(id, JSON.stringify(response));
    }).catch((error: Error) => {
      const response: NativeResponse = { status: 0, headers: {}, body: '', error: error.message };
      if (this.requests.has(id)) this.onComplete(id, JSON.stringify(response));
    }).finally(() => {
      if (this.requests.delete(id)) client.destroy();
    });
  }

  cancel(id: number): void {
    const client = this.requests.get(id);
    if (client) {
      this.requests.delete(id);
      client.destroy();
    }
  }

  close(): void {
    this.requests.forEach((client: http.HttpRequest) => client.destroy());
    this.requests.clear();
  }
}
