package edu.tec.ic6821.checks;

import com.puppycrawl.tools.checkstyle.api.AbstractCheck;
import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.TokenTypes;

import java.util.List;
import java.util.Objects;

public class ControllerRestrictionsCheck extends AbstractCheck {

    private static final String MSG_ERROR_HANDLING = "[IC-6821] El método de controlador debe estar envuelto en un try/catch para el correcto manejo de errores.";
    private static final String MSG_SERVICE_CALLS = "[IC-6821] El método de controlador no debe contener lógica de casos de uso, por tanto no debería realizar más de una llamada a servicios.";

    @Override
    public int[] getDefaultTokens() {
        return new int[]{TokenTypes.METHOD_DEF};
    }

    @Override
    public int[] getAcceptableTokens() {
        return getDefaultTokens();
    }

    @Override
    public int[] getRequiredTokens() {
        return getDefaultTokens();
    }

    @Override
    public void visitToken(DetailAST ast) {
        super.visitToken(ast);

        final DetailAST modifiers = ast.findFirstToken(TokenTypes.MODIFIERS);
        if (modifiers == null) {
            return;
        }

        final DetailAST annotation = modifiers.findFirstToken(TokenTypes.ANNOTATION);
        if (annotation == null) {
            return;
        }

        final DetailAST ident = annotation.findFirstToken(TokenTypes.IDENT);
        if (ident == null || !ident.getText().endsWith("Mapping")) {
            return;
        }

        checkErrorHandling(ast);
        checkServiceCalls(ast);
    }

    private void checkErrorHandling(DetailAST ast) {
        final DetailAST slist = ast.findFirstToken(TokenTypes.SLIST);
        final DetailAST child = slist.getFirstChild();
        if (child.getType() != TokenTypes.LITERAL_TRY) {
            log(slist.getLineNo(), MSG_ERROR_HANDLING);
        }
    }

    private void checkServiceCalls(DetailAST ast) {
        final List<DetailAST> methodCalls = new ASTWalker(ast).traverseCollect(TokenTypes.METHOD_CALL);
        final int serviceCallCount = (int) methodCalls.stream()
            .map(methodCall -> methodCall.findFirstToken(TokenTypes.DOT))
            .filter(Objects::nonNull)
            .map(dot -> dot.findFirstToken(TokenTypes.IDENT))
            .filter(ident -> ident.getText().endsWith("Service"))
            .count();

        if (serviceCallCount > 1) {
            log(ast.getLineNo(), MSG_SERVICE_CALLS);
        }
    }
}
