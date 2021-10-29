package edu.tec.ic6821.checks;

import com.puppycrawl.tools.checkstyle.api.AbstractCheck;
import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.TokenTypes;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class LayeredRestrictionsCheck extends AbstractCheck {

    private static final String MSG_RESTRICTED_IMPORT = "[IC-6821] El import de %s rompe el patrón de capas";
    private static final String MSG_RESTRICTED_REFERENCE = "[IC-6821] La referencia %s rompe el patrón de capas";

    private final Set<String> names = new HashSet<>();
    private final Set<String> importRestrictions = new HashSet<>();
    private final Set<String> referenceRestrictions = new HashSet<>();

    public void setNames(final String... names) {
        this.names.addAll(Arrays.asList(names));
    }

    public void setImportRestrictions(final String... importRestrictions) {
        this.importRestrictions.addAll(Arrays.asList(importRestrictions));
    }

    public void setReferenceRestrictions(final String... referenceRestrictions) {
        this.referenceRestrictions.addAll(Arrays.asList(referenceRestrictions));
    }

    @Override
    public int[] getDefaultTokens() {
        return new int[]{TokenTypes.CLASS_DEF};
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

        System.out.println("Chequeando capas");

        if (this.names.isEmpty()) {
            throw new IllegalArgumentException("Property names is required");
        }

        final DetailAST ident = ast.findFirstToken(TokenTypes.IDENT);
        final List<String> matches = this.names.stream()
            .filter(namePattern -> ident.getText().matches(namePattern))
            .collect(Collectors.toList());
        if (matches.isEmpty()) {
            return;
        }

        checkImportRestrictions(ast);
        checkReferenceRestrictions(ast);
    }

    private void checkImportRestrictions(DetailAST ast) {
        if (this.importRestrictions.isEmpty()) {
            return;
        }

        final List<DetailAST> imports = new ASTWalker(ast).findImports();
        final List<String> importedPackages = imports.stream()
            .map(importAst -> new ASTWalker(importAst).flattenDotExpr())
            .collect(Collectors.toList());

        importedPackages.forEach(importedPackage -> {
            final List<String> matches = this.importRestrictions.stream()
                .filter(importedPackage::matches)
                .collect(Collectors.toList());

            if (!matches.isEmpty()) {
                log(ast.getLineNo(),
                    String.format(MSG_RESTRICTED_IMPORT, importedPackage));
            }
        });
    }

    private void checkReferenceRestrictions(DetailAST ast) {
        if (this.referenceRestrictions.isEmpty()) {
            return;
        }

        final List<DetailAST> types = new ASTWalker(ast).traverseCollect(TokenTypes.TYPE);
        types.forEach(typeAST -> {
            String type = new ASTWalker(typeAST).flattenDotExpr();
            final List<String> matches = this.referenceRestrictions.stream()
                .filter(type::matches)
                .collect(Collectors.toList());

            if (!matches.isEmpty()) {
                log(typeAST.getLineNo(),
                    String.format(MSG_RESTRICTED_REFERENCE, type));
            }
        });
    }
}
