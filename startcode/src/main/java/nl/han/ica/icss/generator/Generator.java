package nl.han.ica.icss.generator;


import nl.han.ica.icss.ast.*;
import nl.han.ica.icss.ast.literals.ColorLiteral;
import nl.han.ica.icss.ast.literals.PercentageLiteral;
import nl.han.ica.icss.ast.literals.PixelLiteral;
import nl.han.ica.icss.transforms.exceptions.InvalidLiteralTypeException;

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

		generateExpression(node.expression);
		stringBuilder.append("; \n");
	}

	private void generateExpression(Expression node) {
		if (node instanceof ColorLiteral) {
			stringBuilder.append(((ColorLiteral) node).value);
		}
		else if (node instanceof PixelLiteral) {
			stringBuilder.append(((PixelLiteral) node).value);
			stringBuilder.append("px");
		}
		else if (node instanceof PercentageLiteral) {
			stringBuilder.append(((PercentageLiteral) node).value);
			stringBuilder.append("%");
		}
		else {
			throw new InvalidLiteralTypeException();
		}
	}
}
