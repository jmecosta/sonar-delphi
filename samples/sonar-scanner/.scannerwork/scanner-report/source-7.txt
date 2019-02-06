\{*
 * Sonar Delphi Plugin
 * Copyright (C) 2011 Sabre Airline Solutions and Fabricio Colombo
 * Author(s):
 * Przemyslaw Kociolek (przemyslaw.kociolek@sabre.com)
 * Michal Wojcik (michal.wojcik@sabre.com)
 * Fabricio Colombo (fabricio.colombo.mva@gmail.com)
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02
 *\}

unit MainWindow;

interface

{** documented class **}
type
    TMainWindow = class(TForm)
    public
        procedure foo1;
        procedure foo2;
    private
        field1: integer;
    protected
        { comment line }
        //another comment
        (* block comment *)
    end;

var
    window: TMainWindow;

implementation

uses
    OverloadTest, FunctionTest;

{$R *.dfm}

procedure TMainWindow.foo1;
begin
    foo2;
end;

procedure TMainWindow.foo2;
begin
    foo1;
    globalProcedure();
    x := globalFunction;
    TOverloadTest.over1(5);
    fooZZ;
    bar;
    getField;
    fooXX;
    getPrivateField();
    TFunctionTest.setField(5);
    TFunctionTest.getField;
    TFunctionTest.foo;
    TFunctionTest.bar();
    rfcFunction(123);
end;

end.