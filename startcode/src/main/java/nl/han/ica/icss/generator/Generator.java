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
		generateStylesheet(ast.root);
		return stringBuilder.toString();
	}

	private void generateStylesheet(Stylesheet node) {
		for (var child : node.getChildren()) {
			if (child instanceof Stylerule) {
				generateStylerule((Stylerule) child);
			}
		}
	}

	private void generateStylerule(Stylerule node) {
		stringBuilder.append(node.selectors.get(0));
		stringBuilder.append(" {");
		stringBuilder.append("\n");

		for (var child : node.getChildren()) {
			if (child instanceof Declaration) {
				generateDeclaration((Declaration) child);
			}
		}

		stringBuilder.append("}\n\n");
	}

	private void generateDeclaration(Declaration node) {
		stringBuilder.append("  ");
		stringBuilder.append(node.property.name);
		stringBuilder.append(": ");

		stringBuilder.append(node.expression);
		stringBuilder.append("; \n");
	}
}
