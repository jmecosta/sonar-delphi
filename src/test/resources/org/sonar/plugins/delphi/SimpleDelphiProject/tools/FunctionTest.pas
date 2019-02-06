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

unit FunctionTest;

interface

type
    TFunctionTest = class(TStatementTest, TForm)
    public
        function getField: integer;
        procedure setField(x: integer);
        procedure foo;
    private
        procedure bar;
        classField: integer;
    end;

var
    window: TFunctionTest;

implementation

procedure TFunctionTest.setField(x: integer);
var
    placeHolder, z: real;
begin
    classField := placeHolder + z + x;
    if(x < 5) then x := 5;
    if(x > 5) then z := 0.0;
end;

function TFunctionTest.getField: integer;
var
    placeHolder: real;
    y: integer;
begin
    result := placeHolder + y;
    y := 0;
    while(true) do
    begin
        break;
    end;
end;

procedure TFunctionTest.foo;
var
    placeHolder, y: real;
begin
    setField(0);
end;

procedure TFunctionTest.bar;
var
    placeHolder, x: real;
begin
    classField := 0;
end;

end.