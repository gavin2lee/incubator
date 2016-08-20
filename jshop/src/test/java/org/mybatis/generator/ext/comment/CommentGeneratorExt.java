package org.mybatis.generator.ext.comment;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.dom.java.*;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.config.PropertyRegistry;
import org.mybatis.generator.internal.DefaultCommentGenerator;

import java.util.Date;
import java.util.Properties;

import static org.mybatis.generator.internal.util.StringUtility.isTrue;

/**
 * 代码注释生成器扩展程序
 * 增加细粒度的注释控制，为每个注释控制设置默认值
 * 修改suppressDate的默认值为true
 */
public class CommentGeneratorExt extends DefaultCommentGenerator{

	/** The properties. */
	private Properties properties;
	/** The suppress date. */
	private boolean suppressDate = true;
	/** The suppress all comments. */
	private boolean suppressAllComments = false;
	/** mapper文件中的element是否增加注释 */
	private boolean suppressElementComments = true;
	/** mapper文件中的根结点是否增加注释 */
	private boolean suppressRootElementComments = true;
	/** java 类是否增加注释 */
	private boolean suppressClassComments = false;
	/** field是否增加注释 */
	private boolean suppressFieldComments = true;
	/** 数据表字段对应的field是否增加注释 */
	private boolean suppressColumnFieldComments = false;
	/** method是否增加注释 */
	private boolean suppressMethodComments = true;
	/** getter方法是否增加注释 */
	private boolean suppressGetterComments = true;
	/** setter方法是否增加注释 */
	private boolean suppressSetterComments = true;

	/**
	 * Instantiates a new default comment generator.
	 */
	public CommentGeneratorExt() {
		super();
		properties = new Properties();
	}

	/* (non-Javadoc)
	 * 生成的注释位于文件最顶端
	 * @see org.mybatis.generator.api.CommentGenerator#addJavaFileComment(org.mybatis.generator.api.dom.java.CompilationUnit)
	 */
	public void addJavaFileComment(CompilationUnit compilationUnit) {
		super.addJavaFileComment(compilationUnit);
	}

	/**
	 * Adds a suitable comment to warn users that the element was generated, and when it was generated.
	 *
	 * @param xmlElement
	 *            the xml element
	 */
	public void addComment(XmlElement xmlElement) {
		if (suppressElementComments) {
			return;
		}
		super.addComment(xmlElement);
	}

	/* (non-Javadoc)
	 * @see org.mybatis.generator.api.CommentGenerator#addRootComment(org.mybatis.generator.api.dom.xml.XmlElement)
	 */
	public void addRootComment(XmlElement rootElement) {
		// add no document level comments by default
		if(suppressRootElementComments) {
			return;
		}
		super.addComment(rootElement);
	}

	/* (non-Javadoc)
	 * @see org.mybatis.generator.api.CommentGenerator#addConfigurationProperties(java.util.Properties)
	 */
	public void addConfigurationProperties(Properties properties) {
		super.addConfigurationProperties(properties);
		this.properties.putAll(properties);
		suppressElementComments = isTrue(properties
				.getProperty("suppressElementComments", String.valueOf(suppressElementComments)));
		suppressRootElementComments = isTrue(properties
				.getProperty("suppressRootElementComments", String.valueOf(suppressRootElementComments)));
		suppressClassComments = isTrue(properties.getProperty("suppressClassComments", String.valueOf(suppressClassComments)));
		suppressFieldComments = isTrue(properties.getProperty("suppressFieldComments", String.valueOf(suppressFieldComments)));
		suppressMethodComments = isTrue(properties.getProperty("suppressMethodComments", String.valueOf(suppressMethodComments)));
		suppressGetterComments = isTrue(properties.getProperty("suppressGetterComments", String.valueOf(suppressGetterComments)));
		suppressSetterComments = isTrue(properties.getProperty("suppressSetterComments", String.valueOf(suppressSetterComments)));
		suppressColumnFieldComments = isTrue(properties.getProperty("suppressColumnFieldComments", String.valueOf(suppressColumnFieldComments)));
		suppressAllComments = isTrue(properties
						.getProperty(PropertyRegistry.COMMENT_GENERATOR_SUPPRESS_ALL_COMMENTS, String.valueOf(suppressAllComments)));
		suppressDate = isTrue(properties
				.getProperty(PropertyRegistry.COMMENT_GENERATOR_SUPPRESS_DATE, String.valueOf(suppressDate)));
	}

	protected String getDateString() {
		if (suppressDate) {
			return null;
		} else {
			return new Date().toString();
		}
	}

	/**
	 * This method adds the custom javadoc tag for. You may do nothing if you do not wish to include the Javadoc tag -
	 * however, if you do not include the Javadoc tag then the Java merge capability of the eclipse plugin will break.
	 *
	 * @param javaElement
	 *            the java element
	 * @param markAsDoNotDelete
	 *            the mark as do not delete
	 */
	protected void addJavadocTag(JavaElement javaElement,
	                             boolean markAsDoNotDelete) {
		super.addJavadocTag(javaElement, markAsDoNotDelete);
	}

	/* (non-Javadoc)
	 * @see org.mybatis.generator.api.CommentGenerator#addClassComment(org.mybatis.generator.api.dom.java.InnerClass, org.mybatis.generator.api.IntrospectedTable)
	 */
	public void addClassComment(InnerClass innerClass,
	                            IntrospectedTable introspectedTable) {
		if (suppressClassComments) {
			return;
		}
		super.addClassComment(innerClass, introspectedTable);
	}

	/* (non-Javadoc)
	 * @see org.mybatis.generator.api.CommentGenerator#addEnumComment(org.mybatis.generator.api.dom.java.InnerEnum, org.mybatis.generator.api.IntrospectedTable)
	 */
	public void addEnumComment(InnerEnum innerEnum,
	                           IntrospectedTable introspectedTable) {
		if(suppressClassComments){
			return;
		}
		super.addEnumComment(innerEnum, introspectedTable);
	}

	/* (non-Javadoc)
	 * @see org.mybatis.generator.api.CommentGenerator#addFieldComment(org.mybatis.generator.api.dom.java.Field, org.mybatis.generator.api.IntrospectedTable, org.mybatis.generator.api.IntrospectedColumn)
	 */
	public void addFieldComment(Field field,
	                            IntrospectedTable introspectedTable,
	                            IntrospectedColumn introspectedColumn) {
		if (suppressColumnFieldComments || suppressAllComments) {
			return;
		}
		StringBuilder sb = new StringBuilder("/** ");

//		field.addJavaDocLine("/**"); //$NON-NLS-1$
//		sb.append(" * "); //$NON-NLS-1$
		sb.append(getFieldComment(introspectedTable, introspectedColumn));
//		field.addJavaDocLine(sb.toString());
		sb.append(" */");
//		addJavadocTag(field, false);

		field.addJavaDocLine(sb.toString()); //$NON-NLS-1$
	}

	private String getFieldComment(IntrospectedTable introspectedTable,
	                               IntrospectedColumn introspectedColumn) {
		String remarks = introspectedColumn.getRemarks();
		if(remarks == null || remarks.isEmpty()) {
			remarks = introspectedTable.getFullyQualifiedTable() + "." + introspectedColumn.getActualColumnName();
		}
		return remarks;
	}

	/* (non-Javadoc)
	 * @see org.mybatis.generator.api.CommentGenerator#addFieldComment(org.mybatis.generator.api.dom.java.Field, org.mybatis.generator.api.IntrospectedTable)
	 */
	public void addFieldComment(Field field, IntrospectedTable introspectedTable) {
		if (suppressFieldComments || suppressAllComments) {
			return;
		}
		super.addFieldComment(field, introspectedTable);
	}

	/* (non-Javadoc)
	 * @see org.mybatis.generator.api.CommentGenerator#addGeneralMethodComment(org.mybatis.generator.api.dom.java.Method, org.mybatis.generator.api.IntrospectedTable)
	 */
	public void addGeneralMethodComment(Method method,
	                                    IntrospectedTable introspectedTable) {
		if (suppressMethodComments) {
			return;
		}
		super.addGeneralMethodComment(method, introspectedTable);
	}

	/* (non-Javadoc)
	 * @see org.mybatis.generator.api.CommentGenerator#addGetterComment(org.mybatis.generator.api.dom.java.Method, org.mybatis.generator.api.IntrospectedTable, org.mybatis.generator.api.IntrospectedColumn)
	 */
	public void addGetterComment(Method method,
	                             IntrospectedTable introspectedTable,
	                             IntrospectedColumn introspectedColumn) {
		if (suppressGetterComments || suppressAllComments) {
			return;
		}

		StringBuilder sb = new StringBuilder();

		method.addJavaDocLine("/**"); //$NON-NLS-1$
		sb.append(" * This method returns the value of the database column "); //$NON-NLS-1$
		sb.append(introspectedTable.getFullyQualifiedTable());
		sb.append('.');
		sb.append(introspectedColumn.getActualColumnName());
		method.addJavaDocLine(sb.toString());

		method.addJavaDocLine(" *"); //$NON-NLS-1$

		sb.setLength(0);
		sb.append(" * @return the value of "); //$NON-NLS-1$
		sb.append(introspectedTable.getFullyQualifiedTable());
		sb.append('.');
		sb.append(introspectedColumn.getActualColumnName());
		method.addJavaDocLine(sb.toString());

		addJavadocTag(method, false);

		method.addJavaDocLine(" */"); //$NON-NLS-1$
	}

	/* (non-Javadoc)
	 * @see org.mybatis.generator.api.CommentGenerator#addSetterComment(org.mybatis.generator.api.dom.java.Method, org.mybatis.generator.api.IntrospectedTable, org.mybatis.generator.api.IntrospectedColumn)
	 */
	public void addSetterComment(Method method,
	                             IntrospectedTable introspectedTable,
	                             IntrospectedColumn introspectedColumn) {
		if (suppressSetterComments || suppressAllComments) {
			return;
		}

		StringBuilder sb = new StringBuilder();

		method.addJavaDocLine("/**"); //$NON-NLS-1$
		sb.append(" * This method sets the value of the database column "); //$NON-NLS-1$
		sb.append(introspectedTable.getFullyQualifiedTable());
		sb.append('.');
		sb.append(introspectedColumn.getActualColumnName());
		method.addJavaDocLine(sb.toString());

		method.addJavaDocLine(" *"); //$NON-NLS-1$

		Parameter parm = method.getParameters().get(0);
		sb.setLength(0);
		sb.append(" * @param "); //$NON-NLS-1$
		sb.append(parm.getName());
		sb.append(" the value for "); //$NON-NLS-1$
		sb.append(introspectedTable.getFullyQualifiedTable());
		sb.append('.');
		sb.append(introspectedColumn.getActualColumnName());
		method.addJavaDocLine(sb.toString());

		addJavadocTag(method, false);

		method.addJavaDocLine(" */"); //$NON-NLS-1$
	}

	/* (non-Javadoc)
	 * @see org.mybatis.generator.api.CommentGenerator#addClassComment(org.mybatis.generator.api.dom.java.InnerClass, org.mybatis.generator.api.IntrospectedTable, boolean)
	 */
	public void addClassComment(InnerClass innerClass,
	                            IntrospectedTable introspectedTable, boolean markAsDoNotDelete) {
		if (suppressClassComments || suppressAllComments) {
			return;
		}
		super.addClassComment(innerClass, introspectedTable, markAsDoNotDelete);
	}

}
