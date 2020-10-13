package nl.han.ica.icss.parser;

import nl.han.ica.datastructures.IHANStack;
import nl.han.ica.datastructures.Stack;
import nl.han.ica.icss.ast.*;
import nl.han.ica.icss.ast.literals.*;
import nl.han.ica.icss.ast.operations.*;
import nl.han.ica.icss.ast.selectors.*;

/**
 * This class extracts the ICSS Abstract Syntax Tree from the Antlr Parse tree.
 */
public class ASTListener extends ICSSBaseListener {
	
	// Accumulator attributes:
	private AST ast;

	// Use this to keep track of the parent nodes when recursively traversing the ast
	private IHANStack<ASTNode> currentContainer;

	public ASTListener() {
		ast = new AST();
		currentContainer = new Stack<>();
	}
    public AST getAST() {
        return ast;
    }

	@Override
	public void enterStylesheet(ICSSParser.StylesheetContext ctx) {
		currentContainer.push(new Stylesheet());
	}

	@Override
	public void exitStylesheet(ICSSParser.StylesheetContext ctx) {
		var stylesheet = currentContainer.pop();
		ast.setRoot((Stylesheet) stylesheet);
	}

	@Override
	public void enterVariableAssignment(ICSSParser.VariableAssignmentContext ctx) {
		currentContainer.push(new VariableAssignment());
	}

	@Override
	public void exitVariableAssignment(ICSSParser.VariableAssignmentContext ctx) {
		var variableAssignment = currentContainer.pop();
		currentContainer.peek().addChild(variableAssignment);
	}

	@Override
	public void enterVariableReference(ICSSParser.VariableReferenceContext ctx) {
		currentContainer.push(new VariableReference(ctx.getText()));
	}

	@Override
	public void exitVariableReference(ICSSParser.VariableReferenceContext ctx) {
		var variableReference = currentContainer.pop();
		currentContainer.peek().addChild(variableReference);
	}

	@Override
	public void enterStylerule(ICSSParser.StyleruleContext ctx) {
		currentContainer.push(new Stylerule());
	}

	@Override
	public void exitStylerule(ICSSParser.StyleruleContext ctx) {
		var stylerule = currentContainer.pop();
		currentContainer.peek().addChild(stylerule);
	}

	@Override
	public void enterIdSelector(ICSSParser.IdSelectorContext ctx) {
		currentContainer.push(new IdSelector(ctx.getText()));
	}

	@Override
	public void exitIdSelector(ICSSParser.IdSelectorContext ctx) {
		var idSelector = currentContainer.pop();
		currentContainer.peek().addChild(idSelector);
	}

	@Override
	public void enterClassSelector(ICSSParser.ClassSelectorContext ctx) {
		currentContainer.push(new ClassSelector(ctx.getText()));
	}

	@Override
	public void exitClassSelector(ICSSParser.ClassSelectorContext ctx) {
		var classSelector = currentContainer.pop();
		currentContainer.peek().addChild(classSelector);
	}

	@Override
	public void enterTagSelector(ICSSParser.TagSelectorContext ctx) {
		currentContainer.push(new TagSelector(ctx.getText()));
	}

	@Override
	public void exitTagSelector(ICSSParser.TagSelectorContext ctx) {
		var tagSelector = currentContainer.pop();
		currentContainer.peek().addChild(tagSelector);
	}

	@Override
	public void enterDeclaration(ICSSParser.DeclarationContext ctx) {
		currentContainer.push(new Declaration());
	}

	@Override
	public void exitDeclaration(ICSSParser.DeclarationContext ctx) {
		var declaration = currentContainer.pop();
		currentContainer.peek().addChild(declaration);
	}

	@Override
	public void enterPropertyName(ICSSParser.PropertyNameContext ctx) {
		currentContainer.push(new PropertyName(ctx.getText()));
	}

	@Override
	public void exitPropertyName(ICSSParser.PropertyNameContext ctx) {
		var propertyName = currentContainer.pop();
		currentContainer.peek().addChild(propertyName);
	}

	@Override
	public void enterBoolLiteral(ICSSParser.BoolLiteralContext ctx) {
		currentContainer.push(new BoolLiteral(ctx.getText()));
	}

	@Override
	public void exitBoolLiteral(ICSSParser.BoolLiteralContext ctx) {
		var boolLiteral = currentContainer.pop();
		currentContainer.peek().addChild(boolLiteral);
	}

	@Override
	public void enterColorLiteral(ICSSParser.ColorLiteralContext ctx) {
		currentContainer.push(new ColorLiteral(ctx.getText()));
	}

	@Override
	public void exitColorLiteral(ICSSParser.ColorLiteralContext ctx) {
		var colorLiteral = currentContainer.pop();
		currentContainer.peek().addChild(colorLiteral);
	}

	@Override
	public void enterPercentageLiteral(ICSSParser.PercentageLiteralContext ctx) {
		currentContainer.push(new PercentageLiteral(ctx.getText()));
	}

	@Override
	public void exitPercentageLiteral(ICSSParser.PercentageLiteralContext ctx) {
		var percentageLiteral = currentContainer.pop();
		currentContainer.peek().addChild(percentageLiteral);
	}

	@Override
	public void enterPixelLiteral(ICSSParser.PixelLiteralContext ctx) {
		currentContainer.push(new PixelLiteral(ctx.getText()));
	}

	@Override
	public void exitPixelLiteral(ICSSParser.PixelLiteralContext ctx) {
		var pixelLiteral = currentContainer.pop();
		currentContainer.peek().addChild(pixelLiteral);
	}

	@Override
	public void enterScalarLiteral(ICSSParser.ScalarLiteralContext ctx) {
		currentContainer.push(new ScalarLiteral(ctx.getText()));
	}

	@Override
	public void exitScalarLiteral(ICSSParser.ScalarLiteralContext ctx) {
		var scalarLiteral = currentContainer.pop();
		currentContainer.peek().addChild(scalarLiteral);
	}

	@Override
	public void enterIfClause(ICSSParser.IfClauseContext ctx) {
		currentContainer.push(new IfClause());
	}

	@Override
	public void exitIfClause(ICSSParser.IfClauseContext ctx) {
		var ifClause = currentContainer.pop();
		currentContainer.peek().addChild(ifClause);
	}

	@Override
	public void enterElseClause(ICSSParser.ElseClauseContext ctx) {
		currentContainer.push(new ElseClause());
	}

	@Override
	public void exitElseClause(ICSSParser.ElseClauseContext ctx) {
		var elseClause = currentContainer.pop();
		currentContainer.peek().addChild(elseClause);
	}

	@Override
	public void enterExpression(ICSSParser.ExpressionContext ctx) {
		if (ctx.getChildCount() == 3) {
			Operation operation = null;
			if (ctx.getChild(1).getText().equals("*")) {
				operation = new MultiplyOperation();
			}
			if (ctx.getChild(1).getText().equals("+")) {
				operation = new AddOperation();
			}
			if (ctx.getChild(1).getText().equals("-")) {
				operation = new SubtractOperation();
			}
			currentContainer.push(operation);
		}
	}

	@Override
	public void exitExpression(ICSSParser.ExpressionContext ctx) {
		if (ctx.getChildCount() == 3) {
			var operation = currentContainer.pop();
			currentContainer.peek().addChild(operation);
		}
	}
}
