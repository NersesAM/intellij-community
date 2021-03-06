/*
 * Copyright 2000-2011 JetBrains s.r.o.
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
package com.intellij.codeInsight.daemon.quickFix;

import com.intellij.codeInsight.template.TemplateManager;
import com.intellij.codeInsight.template.impl.TemplateManagerImpl;
import com.intellij.codeInsight.template.impl.TemplateState;

/**
 * @author anna
 */
public class DelegateWithDefaultParamValueTest extends LightQuickFixTestCase {
  @Override
  protected void doAction(String text, boolean actionShouldBeAvailable, String testFullPath, String testName)
    throws Exception {
    try {
      ((TemplateManagerImpl)TemplateManager.getInstance(getProject())).setTemplateTesting(true);
      super.doAction(text, actionShouldBeAvailable, testFullPath, testName);

      if (actionShouldBeAvailable) {
        TemplateState state = TemplateManagerImpl.getTemplateState(getEditor());
        assert state != null;
        state.gotoEnd(false);
      }
    } finally {
      ((TemplateManagerImpl)TemplateManager.getInstance(getProject())).setTemplateTesting(false);
    }
  }

  public void test() throws Exception {
    doAllTests();

  }

  @Override
  protected String getBasePath() {
    return "/codeInsight/daemonCodeAnalyzer/quickFix/delegateWithDefaultValue";
  }
}
