package mycompiler.lexical;

public enum DfaState {
    INIT, ID, GT, GE, EQ, NUM, ASSIGNMENT,
    PLUS, MINUS, STAR, SLASH,
    INT_1, INT_2, INT_3,
    ;
}
