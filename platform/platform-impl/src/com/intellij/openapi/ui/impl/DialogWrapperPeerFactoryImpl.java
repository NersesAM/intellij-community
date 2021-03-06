/*
 * Copyright 2000-2009 JetBrains s.r.o.
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
package com.intellij.openapi.ui.impl;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.DialogWrapperPeer;
import com.intellij.openapi.ui.DialogWrapperPeerFactory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;

public class DialogWrapperPeerFactoryImpl extends DialogWrapperPeerFactory {
  public DialogWrapperPeer createPeer(DialogWrapper wrapper, @Nullable Project project, boolean canBeParent) {
    return new DialogWrapperPeerImpl(wrapper, project, canBeParent);
  }

  public DialogWrapperPeer createPeer(DialogWrapper wrapper, boolean canBeParent) {
    return new DialogWrapperPeerImpl(wrapper, canBeParent);
  }

  public DialogWrapperPeer createPeer(final DialogWrapper wrapper, final boolean canBeParent, final boolean tryToolkitModal) {
    return new DialogWrapperPeerImpl(wrapper, canBeParent, tryToolkitModal);
  }

  public DialogWrapperPeer createPeer(DialogWrapper wrapper, @NotNull Component parent, boolean canBeParent) {
    return new DialogWrapperPeerImpl(wrapper, parent, canBeParent);
  }
}