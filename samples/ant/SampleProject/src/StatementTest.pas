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

unit StatementTest;

interface

type
    TStatementTest = class(TForm)
    public
        procedure fooZZ;
    private
        procedure bar;
    end;

var
    window: TMainWindow;

implementation

procedure TStatementTest.fooZZ;
var
    x, y: integer;
begin
    x := 0;
    y := 1;
    x := y + 5;
    y := x - 6;
end;

procedure TStatementTest.bar;
begin
    if(5 > 4) then
    begin
        writeln('5 > 4')
    end
    else
    begin
        writeln('5 <= 4');
    end;

    while(true = true) do
        writeln('endless loop');
end;

end.