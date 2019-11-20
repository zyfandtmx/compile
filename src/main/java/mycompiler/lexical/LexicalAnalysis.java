package mycompiler.lexical;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class LexicalAnalysis {
    private List<Token> analysis(String code) {
        List<Token> tokens = new ArrayList<>();
        if (StringUtils.isBlank(code)) {
            return tokens;
        }
        code = code + " ";
        char[] chars = code.toCharArray();
        StringBuilder sb = null;
        DfaState dfaState = DfaState.INIT;
        for (char aChar : chars) {
            switch (dfaState) {
                case INIT: {
                    if (isAlpha(aChar)) {
                        dfaState = DfaState.ID;
                    } else if (aChar == '>') {
                        dfaState = DfaState.GT;
                    } else if (isNum(aChar)) {
                        dfaState = DfaState.NUM;
                    } else if (aChar == '=') {
                        dfaState = DfaState.ASSIGNMENT;
                    }
                    sb = new StringBuilder(aChar + "");
                    break;
                }
                case ID: {
                    if (isAlpha(aChar) || isNum(aChar)) {
                        sb.append(aChar);
                    } else {
                        tokens.add(new Token(sb.toString(), TokenType.IDENTIFIER));
                        dfaState = DfaState.INIT;
                    }
                    break;
                }
                case GT: {
                    if (aChar == '=') {
                        sb.append(aChar);
                        tokens.add(new Token(sb.toString(), TokenType.GE));
                        dfaState = DfaState.INIT;
                    } else {
                        tokens.add(new Token(sb.toString(), TokenType.GT));
                        dfaState = DfaState.INIT;
                    }
                    break;
                }
                case NUM: {
                    if (isNum(aChar)) {
                        sb.append(aChar);
                    } else {
                        tokens.add(new Token(sb.toString(), TokenType.INTLITERAL));
                        dfaState = DfaState.INIT;
                    }
                    break;
                }
                case ASSIGNMENT: {
                    if (aChar == '=') {
                        sb.append(aChar);
                        tokens.add(new Token(sb.toString(), TokenType.EQ));
                        dfaState = DfaState.INIT;
                    } else {
                        tokens.add(new Token(sb.toString(), TokenType.ASSIGNMENT));
                        dfaState = DfaState.INIT;
                    }
                    break;
                }
                default:
            }
        }
        return tokens;
    }

    private boolean isNum(char aChar) {
        return aChar >= '0' && aChar <= '9';
    }

    private boolean isAlpha(char aChar) {
        return (aChar >= 'a' && aChar <= 'z' || aChar >= 'A' && aChar <= 'Z');
    }

    public static void main(String[] args) {
        String testCode1 = "a >= 35";
        String testCode2 = "int a = 40";
        String testCode3 = "age = 40";
        List<Token> analysis = new LexicalAnalysis().analysis(testCode1);
        System.out.println(String.format("%-10s      %-10s", "VALUE", "TYPE"));
        for (Token token : analysis) {
            System.out.println(String.format("%-10s      %-10s", token.getValue(), token.getType()));
        }
    }
}
