package nl.han.ica.icss.generator;


import nl.han.ica.icss.ast.AST;
import nl.han.ica.icss.ast.Declaration;
import nl.han.ica.icss.ast.Stylerule;
import nl.han.ica.icss.ast.Stylesheet;

public class Generator {

	StringBuilder stringBuilder;

	public Generator() {
		stringBuilder = new StringBuilder();
	}

	public String generate(AST ast) {
		return generateStylesheet(ast.root);
	}

	private String generateStylesheet(Stylesheet node) {
		for (var child : node.getChildren()) {
		}

		return "";
	}

	private String generateStylerule(Stylerule node) {

		return "";
	}

	private String generateDeclaration(Declaration node) {

		return "";
	}

	
}
