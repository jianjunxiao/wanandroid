package com.xiaojianjun.wanandroid.platform

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity

@JsFun("""(id) => {
  const frame = document.createElement('iframe');
  frame.id = id;
  frame.title = '文章';
  frame.setAttribute('referrerpolicy', 'no-referrer');
  frame.setAttribute('sandbox', 'allow-scripts allow-same-origin allow-forms allow-popups');
  frame.style.cssText = 'position:fixed;border:0;z-index:2;background:white;transform-origin:top left;';
  document.body.appendChild(frame);
}""")
private external fun createArticleFrame(id: String)

@JsFun("""(id, url, zoom, x, y, width, height) => {
  const frame = document.getElementById(id);
  if (!frame) return;
  const scale = zoom / 100;
  if (frame.getAttribute('src') !== url) frame.src = url;
  Object.assign(frame.style, {
    left: x + 'px', top: y + 'px', width: width / scale + 'px', height: height / scale + 'px',
    transform: 'scale(' + scale + ')'
  });
}""")
private external fun updateArticleFrame(
    id: String, url: String, zoom: Int, x: Double, y: Double, width: Double, height: Double,
)

@JsFun("(id) => document.getElementById(id)?.remove()")
private external fun removeArticleFrame(id: String)
private var frameCounter = 0

@Composable
actual fun ArticleWebView(url: String, textZoom: Int, modifier: Modifier) {
    val id = remember { "wan-article-${++frameCounter}" }
    val density = LocalDensity.current.density
    var bounds by remember { mutableStateOf(Rect.Zero) }
    val frameBounds = bounds
    DisposableEffect(id) {
        createArticleFrame(id)
        onDispose { removeArticleFrame(id) }
    }
    SideEffect {
        updateArticleFrame(
            id, url, textZoom,
            (frameBounds.left / density).toDouble(), (frameBounds.top / density).toDouble(),
            (frameBounds.width / density).toDouble(), (frameBounds.height / density).toDouble(),
        )
    }
    Box(modifier.onGloballyPositioned { bounds = it.boundsInWindow() })
}
