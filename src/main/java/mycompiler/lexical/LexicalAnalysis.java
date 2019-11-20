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
        List<Token> analysis = new LexicalAnalysis().analysis(testCode3);
        System.out.println(String.format("%-10s  %-10s", "VALUE", "TYPE"));
        for (Token token : analysis) {
            System.out.println(String.format("%-10s  %-10s", token.getValue(), token.getType()));
        }
    }
}
