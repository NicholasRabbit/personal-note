package com.jeesite.modules.common.util;

import com.vdurmont.emoji.EmojiManager;
import com.vdurmont.emoji.EmojiParser;
import org.apache.commons.lang.StringUtils;
 
import java.util.List;
 
/**
 * 表情处理工具
 *
 * @Author: szw
 * @Date: 2020/9/15 11:27
 */
public class FilterEmojiUtil extends EmojiParser {
 
    /**
     * 获取非表情字符串
     *
     * @param input String
     * @return String
     */
    public static String getNonEmojiString(String input) {
        int prev = 0;
        StringBuilder sb = new StringBuilder();
        List<EmojiParser.UnicodeCandidate> replacements = getUnicodeCandidates(input);
        for (EmojiParser.UnicodeCandidate candidate : replacements) {
            sb.append(input.substring(prev, candidate.getEmojiStartIndex()));
            prev = candidate.getFitzpatrickEndIndex();
        }
        return sb.append(input.substring(prev)).toString();
    }
 
    /**
     * 获取表情字符串
     *
     * @param input String
     * @return String
     */
    public static String getEmojiUnicodeString(String input) {
        EmojiParser.EmojiTransformer transformer = unicodeCandidate -> unicodeCandidate.getEmoji().getHtmlHexadecimal();
        StringBuilder sb = new StringBuilder();
        List<EmojiParser.UnicodeCandidate> replacements = getUnicodeCandidates(input);
        for (EmojiParser.UnicodeCandidate candidate : replacements) {
            sb.append(transformer.transform(candidate));
        }
        return parseToUnicode(sb.toString());
    }
 
    public static String getUnicode(String source) {
        String returnUniCode = null;
        String uniCodeTemp;
        for (int i = 0; i < source.length(); i++) {
            uniCodeTemp = "\\u" + Integer.toHexString(source.charAt(i));
            returnUniCode = returnUniCode == null ? uniCodeTemp : returnUniCode + uniCodeTemp;
        }
        return returnUniCode;
    }
 
    /**
     * emoji表情替换
     *
     * @param source  原字符串
     * @param slipStr emoji表情替换成的字符串
     * @return 过滤后的字符串
     */
    public static String filterEmoji(String source, String slipStr) {
        if (StringUtils.isNotBlank(source)) {
            return source.replaceAll("[\\ud800\\udc00-\\udbff\\udfff\\ud800-\\udfff]", slipStr);
        } else {
            return source;
        }
    }
 
    /**
     * 判断是否包含emoji表情
     *
     * @param source 原字符串
     *               // @param slipStr emoji表情替换成的字符串
     * @return 过滤后的字符串
     */
    public static boolean checkEmoji(String source) {
        if (StringUtils.isNotBlank(source)) {
            return source.matches("[\\ud800\\udc00-\\udbff\\udfff\\ud800-\\udfff]");
        } else {
            return false;
        }
    }

    //判断是否emoji方法二,有效
    public static boolean hasEmoji(String source){
        return EmojiManager.containsEmoji(source);
    }

    public static void main(String[] args) {
        String content = "带表情的\uD83D\uDE41\uD83D\uDE2B\uD83E\uDD14\uD83D\uDE10";
        System.out.println(getEmojiUnicodeString(content));
        //转换表情
        if (StringUtils.isNotBlank(getEmojiUnicodeString(content))) {
            String hexadecimal = EmojiParser.parseToHtmlHexadecimal(content);
            System.out.println(hexadecimal);
            //转换表情
            String interactContent = EmojiParser.parseToUnicode(hexadecimal);
            System.out.println(interactContent);
        }
    }
}

