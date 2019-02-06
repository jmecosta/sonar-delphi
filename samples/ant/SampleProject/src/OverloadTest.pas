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

unit OverloadTest;

interface

type
    TOverloadTest = class
    public
        function over1(x, y: integer): integer;
        function over1(x: integer; y: real): integer; overload;
        function over1(x: real): integer; overload;
        function over1(x: float): integer; overload;
        function notOver(): real;
    private
        field: integer;
    end;

implementation

{$R *.dfm}

function TOverloadTest.notOver(): real;
begin
    result := 5.0;
end;

function TOverloadTest.over1(x, y: integer): integer;
begin
    field := x + y;
    over1(2.5);
end;

function TOverloadTest.over1(x: integer; y: real): integer;
begin
    field := 0;
end;

function TOverloadTest.over1(x: real): integer;
begin
    result := 3;
end;

function TOverloadTest.over1(x: float): integer;
begin
    result := 4;
end;

end.