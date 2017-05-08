package fr.pinguet62.xjc.common.test;

import java.lang.annotation.Annotation;
import java.lang.annotation.Repeatable;
import java.lang.reflect.Field;

import com.github.javaparser.ast.body.BodyDeclaration;
import com.github.javaparser.ast.body.EnumConstantDeclaration;
import com.github.javaparser.ast.body.EnumDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.AnnotationExpr;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.MarkerAnnotationExpr;
import com.github.javaparser.ast.expr.MemberValuePair;
import com.github.javaparser.ast.expr.NormalAnnotationExpr;
import com.github.javaparser.ast.expr.SingleMemberAnnotationExpr;

public class JavaParserUtils {

    /**
     * @return The first found (doesn't support {@link Repeatable}).<br>
     *         {@code null} if not found.
     */
    public static AnnotationExpr findAnnotation(BodyDeclaration enumConstant, Class<? extends Annotation> annotation) {
        for (AnnotationExpr a : enumConstant.getAnnotations())
            if (a.getName().getName().equals(annotation.getSimpleName()))
                return a;
        return null;
    }


    /**
     * @return {@code null} if not found.<br>
     *         {@code null} if invalid parameter name.
     */
    public static Expression findArgument(AnnotationExpr annotation, String key) {
        if (annotation instanceof MarkerAnnotationExpr)
            return null;
        else if (annotation instanceof SingleMemberAnnotationExpr) {
            SingleMemberAnnotationExpr singleMemberAnnotation = (SingleMemberAnnotationExpr) annotation;
            if (!key.equals("value")) // name for annotation with single parameter
                return null;
            return singleMemberAnnotation.getMemberValue();
        } else if (annotation instanceof NormalAnnotationExpr) {
            NormalAnnotationExpr normalAnnotation = (NormalAnnotationExpr) annotation;
            for (MemberValuePair memberValue : normalAnnotation.getPairs())
                if (memberValue.getName().equals(key))
                    return memberValue.getValue();
            return null;
        } else
            throw new UnsupportedOperationException("Unknown annotation type: " + annotation.getClass());
    }

    /** @return {@code null} if not found. */
    public static EnumConstantDeclaration findEntry(EnumDeclaration type, String entry) {
        for (EnumConstantDeclaration enumConstant : type.getEntries())
            if (enumConstant.getName().equals(entry))
                return enumConstant;
        return null;
    }

    /**
     * @param name
     *            The {@link Field#getName()}.
     * @return {@code null} if not found.
     */
    public static FieldDeclaration findField(TypeDeclaration type, String name) {
        for (BodyDeclaration member : type.getMembers())
            if (member instanceof FieldDeclaration) {
                FieldDeclaration field = (FieldDeclaration) member;
                VariableDeclarator variable = field.getVariables().get(0);
                if (variable.getId().getName().equals(name))
                    return field;
            }
        return null;
    }

    /**
     * @return The first found (naming collision).<br>
     *         {@code null} if not found.
     */
    public static MethodDeclaration findMethod(TypeDeclaration type, String name) {
        for (BodyDeclaration member : type.getMembers())
            if (member instanceof MethodDeclaration) {
                MethodDeclaration method = (MethodDeclaration) member;
                if (method.getName().equals(name))
                    return method;
            }
        return null;
    }

}