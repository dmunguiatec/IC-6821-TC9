package edu.tec.ic6821.checks;

import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.TokenTypes;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class ASTWalker {

    final class Recursive<F> {
        F lambda;
    }

    private static final String TYPE_COMMA_REPLACEMENT = "~";

    private final DetailAST ast;

    public ASTWalker(final DetailAST ast) {
        this.ast = ast;
    }

    public List<DetailAST> filterTokensByType(final int tokenType) {
        return filterTokensByType(this.ast, tokenType);
    }

    private List<DetailAST> filterTokensByType(final DetailAST ast, final int tokenType) {
        final List<DetailAST> tokens = new ArrayList<>();
        if (ast != null) {
            DetailAST child = ast.getFirstChild();
            while (child != null) {
                if (child.getType() == tokenType) {
                    tokens.add(child);
                }
                child = child.getNextSibling();
            }
        }

        return tokens;
    }

    public String flattenType() {
        if (this.ast.getType() != TokenTypes.TYPE) {
            return null;
        }

        final StringBuilder builder = new StringBuilder();

        final Recursive<Consumer<DetailAST>> traverse = new Recursive<>();
        traverse.lambda = (DetailAST node) -> {
            if (!node.hasChildren()) {
                final String text = node.getType() == TokenTypes.COMMA ? TYPE_COMMA_REPLACEMENT : node.getText();
                builder.append(text);
            } else {
                // these are non-leaf nodes linked to literal tokens
                if (node.getType() == TokenTypes.TYPE_UPPER_BOUNDS
                    || node.getType() == TokenTypes.TYPE_LOWER_BOUNDS
                    || node.getType() == TokenTypes.ARRAY_DECLARATOR) {
                    builder.append(node.getText());
                }
                DetailAST child = node.getFirstChild();
                while (child != null) {
                    traverse.lambda.accept(child);
                    child = child.getNextSibling();
                }
            }
        };

        traverse.lambda.accept(this.ast);
        return builder.toString();
    }

    public DetailAST findPackageDef() {
        DetailAST sibling = this.ast.getPreviousSibling();
        while (sibling != null) {
            if (sibling.getType() == TokenTypes.PACKAGE_DEF) {
                return sibling;
            }
            sibling = sibling.getPreviousSibling();
        }

        return null;
    }

    public List<DetailAST> findImports() {
        List<DetailAST> imports = new ArrayList<>();
        DetailAST sibling = this.ast.getPreviousSibling();
        while (sibling != null) {
            if (sibling.getType() == TokenTypes.IMPORT) {
                imports.add(sibling);
            }
            sibling = sibling.getPreviousSibling();
        }

        return imports;
    }

    public String flattenDotExpr() {
        if (this.ast.getType() != TokenTypes.IMPORT
            && this.ast.getType() != TokenTypes.PACKAGE_DEF
            && this.ast.getType() != TokenTypes.TYPE) {
            return null;
        }

        final List<String> tokens = new ArrayList<>();

        final Recursive<Consumer<DetailAST>> traverse = new Recursive<>();
        traverse.lambda = (DetailAST node) -> {
            final DetailAST dot = node.findFirstToken(TokenTypes.DOT);
            if (dot != null) {
                traverse.lambda.accept(dot);
            }

            final List<DetailAST> idents = filterTokensByType(node, TokenTypes.IDENT);
            idents.forEach(ident -> tokens.add(ident.getText()));
        };

        traverse.lambda.accept(this.ast);
        System.out.println("tokens = " + tokens);
        return String.join(".", tokens);
    }

    public List<DetailAST> traverseCollect(final int tokenType) {
        final List<DetailAST> tokens = new ArrayList<>();

        final Recursive<Consumer<DetailAST>> traverse = new Recursive<>();
        traverse.lambda = (DetailAST node) -> {
            if (node != null) {
                DetailAST child = node.getFirstChild();
                while (child != null) {
                    if (child.getType() == tokenType) {
                        tokens.add(child);
                    }

                    if (child.hasChildren()) {
                        traverse.lambda.accept(child);
                    }

                    child = child.getNextSibling();
                }
            }
        };

        traverse.lambda.accept(this.ast);
        return tokens;
    }
}
