package com.xiaojianjun.wanandroid.ui.compose.common

import kotlin.test.Test
import kotlin.test.assertEquals

class HtmlTextTest {
    @Test
    fun namedEntitiesContainingDigitsAreDecoded() {
        assertEquals("½ ¼ ¾ ¹ ² ³ ∴", "&frac12; &frac14; &frac34; &sup1; &sup2; &sup3; &there4;".htmlPlainText())
    }

    @Test
    fun quotedTagAttributesAndLiteralComparisonsPreserveTitleText() {
        assertEquals("标题 1 < 2 > 0", "<a title='1 > 0'>标题</a> 1 < 2 > 0<!-- 隐藏 -->".htmlPlainText())
    }

    @Test
    fun titlePreservesEntitiesAndEmphasizedText() {
        assertEquals("Compose & Kotlin <开发> \"测试\"", "<b>Compose &amp; Kotlin</b> &lt;开发&gt; &quot;测试&quot;".htmlPlainText())
    }

    @Test
    fun lineBreaksAndSupplementaryCharactersAreDecoded() {
        assertEquals("第一行\n第二行😀😀", "第一行<br/>第二行&#x1F600;&#X1F600;".htmlPlainText())
    }

    @Test
    fun invalidEntitiesDoNotCreateInvalidCharacters() {
        assertEquals("&#xD800; &unknown;", "&#xD800; &unknown;".htmlPlainText())
        assertEquals("", (null as String?).htmlPlainText())
    }
}
