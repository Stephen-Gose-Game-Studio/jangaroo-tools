/*
 * Copyright 2008 CoreMedia AG
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0 
 * 
 * Unless required by applicable law or agreed to in writing, 
 * software distributed under the License is distributed on an "AS
 * IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either 
 * express or implied. See the License for the specific language 
 * governing permissions and limitations under the License.
 */

package net.jangaroo.jooc.ast;

import net.jangaroo.jooc.JooSymbol;
import net.jangaroo.jooc.Scope;

import java.io.IOException;

/**
 * @author Andreas Gawecki
 */
public class DefaultStatement extends Statement {

  private JooSymbol symDefault;
  private JooSymbol symColon;

  public DefaultStatement(JooSymbol symDefault, JooSymbol symColon) {
    this.setSymDefault(symDefault);
    this.setSymColon(symColon);
  }

  @Override
  public void visit(AstVisitor visitor) throws IOException {
    visitor.visitDefaultStatement(this);
  }

  @Override
  public void scope(final Scope scope) {
  }

  public JooSymbol getSymbol() {
    return getSymDefault();
  }


  public JooSymbol getSymDefault() {
    return symDefault;
  }

  public void setSymDefault(JooSymbol symDefault) {
    this.symDefault = symDefault;
  }

  public JooSymbol getSymColon() {
    return symColon;
  }

  public void setSymColon(JooSymbol symColon) {
    this.symColon = symColon;
  }
}
