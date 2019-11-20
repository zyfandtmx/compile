package mycompiler.lexical;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 词法解析器
 *
 * @author zyf
 */
public class LexicalAnalysis {

    /**
     * <p>将代码解析为token</p>
     *
     * @param code 待处理简单代码串
     * @return 处理后的token列表
     */
    private List<Token> analysis(String code) {
        List<Token> tokens = new ArrayList<>();
        if (StringUtils.isBlank(code)) {
            return tokens;
        }
        code = code + " "; //哨兵空格，此处代替‘;’的功能，统一处理结尾
        char[] chars = code.toCharArray();
        StringBuilder sb = null;
        DfaState dfaState = DfaState.INIT;
        //有限自动机循环处理每一个字符
        for (char aChar : chars) {
            switch (dfaState) {
                case INIT: {
                    dfaState = initState(aChar);
                    sb = new StringBuilder(aChar + "");
                    break;
                }
                case ID: {
                    if (isAlpha(aChar) || isNum(aChar)) {
                        sb.append(aChar);
                    } else {
                        tokens.add(new Token(sb.toString(), TokenType.IDENTIFIER));
                        dfaState = initState(aChar);
                        sb = new StringBuilder(aChar + "");
                    }
                    break;
                }
                case GT: {
                    if (aChar == '=') {
                        sb.append(aChar);
                        dfaState = DfaState.GE;
                    } else {
                        tokens.add(new Token(sb.toString(), TokenType.GT));
                        dfaState = initState(aChar);
                        sb = new StringBuilder(aChar + "");
                    }
                    break;
                }
                case GE: {
                    tokens.add(new Token(sb.toString(), TokenType.GE));
                    dfaState = initState(aChar);
                    sb = new StringBuilder(aChar + "");
                    break;
                }
                case NUM: {
                    if (isNum(aChar)) {
                        sb.append(aChar);
                    } else {
                        tokens.add(new Token(sb.toString(), TokenType.INTLITERAL));
                        dfaState = initState(aChar);
                        sb = new StringBuilder(aChar + "");
                    }
                    break;
                }
                case ASSIGNMENT: {
                    if (aChar == '=') {
                        sb.append(aChar);
                        tokens.add(new Token(sb.toString(), TokenType.EQ));
                        dfaState = initState(aChar);
                        sb = new StringBuilder(aChar + "");
                    } else {
                        tokens.add(new Token(sb.toString(), TokenType.ASSIGNMENT));
                        dfaState = initState(aChar);
                        sb = new StringBuilder(aChar + "");
                    }
                    break;
                }
                case PLUS: {
                    tokens.add(new Token(sb.toString(), TokenType.PLUS));
                    dfaState = initState(aChar);
                    sb = new StringBuilder(aChar + "");
                    break;
                }
                case MINUS: {
                    tokens.add(new Token(sb.toString(), TokenType.MINUS));
                    dfaState = initState(aChar);
                    sb = new StringBuilder(aChar + "");
                    break;
                }
                case STAR: {
                    tokens.add(new Token(sb.toString(), TokenType.STAR));
                    dfaState = initState(aChar);
                    sb = new StringBuilder(aChar + "");
                    break;
                }
                case SLASH: {
                    tokens.add(new Token(sb.toString(), TokenType.SLASH));
                    dfaState = initState(aChar);
                    sb = new StringBuilder(aChar + "");
                    break;
                }
                default:
            }
        }
        return tokens;
    }

    private DfaState initState(char aChar) {
        if (isAlpha(aChar)) {
            return DfaState.ID;
        } else if (aChar == '>') {
            return DfaState.GT;
        } else if (isNum(aChar)) {
            return DfaState.NUM;
        } else if (aChar == '=') {
            return DfaState.ASSIGNMENT;
        } else if (aChar == '+') {
            return DfaState.PLUS;
        } else if (aChar == '-') {
            return DfaState.MINUS;
        } else if (aChar == '*') {
            return DfaState.STAR;
        } else if (aChar == '/') {
            return DfaState.SLASH;
        }
        return DfaState.INIT;
    }

    private boolean isNum(char aChar) {
        return aChar >= '0' && aChar <= '9';
    }

    private boolean isAlpha(char aChar) {
        return (aChar >= 'a' && aChar <= 'z' || aChar >= 'A' && aChar <= 'Z');
    }

    public static void main(String[] args) {
        String testCode1 = "a >= 35";
        String testCode2 = "int a=40";
        String testCode3 = "age=40";
        String testCode4 = "3+ 4 * 5 /6 -7";

        List<Token> analysis = new LexicalAnalysis().analysis(testCode4);
        System.out.println(String.format("%-10s  %-10s", "VALUE", "TYPE"));
        for (Token token : analysis) {
            System.out.println(String.format("%-10s  %-10s", token.getValue(), token.getType()));
        }
    }
}
