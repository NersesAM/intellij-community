/*
 * Copyright 2000-2012 JetBrains s.r.o.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.intellij.codeInsight.daemon.impl.quickfix;

import com.intellij.codeInsight.CodeInsightUtilBase;
import com.intellij.codeInsight.ExpectedTypeInfo;
import com.intellij.codeInsight.template.Template;
import com.intellij.codeInsight.template.TemplateBuilderImpl;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.*;
import com.intellij.psi.impl.source.codeStyle.CodeEditUtil;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.refactoring.introduceField.BaseExpressionToFieldHandler;

/**
 * @author Max Medvedev
 */
public class JavaCreateFieldFromUsageHelper extends CreateFieldFromUsageHelper {

  @Override
  public Template setupTemplateImpl(PsiField field,
                                    Object expectedTypes,
                                    PsiClass targetClass,
                                    Editor editor,
                                    PsiElement context,
                                    boolean createConstantField,
                                    PsiSubstitutor substitutor) {
    PsiElementFactory factory = JavaPsiFacade.getElementFactory(field.getProject());

    field = CodeInsightUtilBase.forcePsiPostprocessAndRestoreElement(field);
    TemplateBuilderImpl builder = new TemplateBuilderImpl(field);
    if (!(expectedTypes instanceof ExpectedTypeInfo[])) {
      expectedTypes = ExpectedTypeInfo.EMPTY_ARRAY;
    }
    new GuessTypeParameters(factory).setupTypeElement(field.getTypeElement(), (ExpectedTypeInfo[])expectedTypes, substitutor, builder,
                                                      context, targetClass);

    if (createConstantField) {
      field.setInitializer(factory.createExpressionFromText("0", null));
      builder.replaceElement(field.getInitializer(), new EmptyExpression());
      PsiIdentifier identifier = field.getNameIdentifier();
      builder.setEndVariableAfter(identifier);
      field = CodeInsightUtilBase.forcePsiPostprocessAndRestoreElement(field);
    }
    editor.getCaretModel().moveToOffset(field.getTextRange().getStartOffset());
    Template template = builder.buildInlineTemplate();
    if (((ExpectedTypeInfo[])expectedTypes).length > 1) template.setToShortenLongNames(false);
    return template;
  }

  @Override
  public PsiField insertFieldImpl(PsiClass targetClass, PsiField field, PsiElement place) {
    PsiMember enclosingContext = null;
    PsiClass parentClass;
    do {
      enclosingContext = PsiTreeUtil.getParentOfType(enclosingContext == null ? place : enclosingContext, PsiMethod.class, PsiField.class, PsiClassInitializer.class);
      parentClass = enclosingContext == null ? null : enclosingContext.getContainingClass();
    }
    while (parentClass instanceof PsiAnonymousClass);

    return BaseExpressionToFieldHandler.ConvertToFieldRunnable.appendField(targetClass, field, enclosingContext, null);
  }

}
