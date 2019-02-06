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

unit DemoForm;

interface

type
    TDemo = class(TMyClass, TMyElder, TMyAncestor)
    private
        bShowTracker: TButton;
    public
        procedure bShowTrackerClick();
        function getFunction(): integer;
        field1, field2: integer;
    protected
        bShowTracker: TButton;
    end;

type
    TMyClass = class
        procedure myProcedure();
        procedure setSomething();
    protected
        protectedField: real;
    end;

type
    TMyAncestor = class(TMyElder)
    end;

type
    TMyElder = class
    end;

var
    fDemo1, fDemo2: TfDemo;
    bigInt: Integer;

implementation

{***
**** TDEMO
***}
function TDemo.getFunction(): integer;
begin
    result := 7;
    x := 5;
end;

procedure TDemo.bShowTrackerClick();
var
    str: string;
begin
    str := 'sample string';
end;

{***
**** TMYCLASS
***}
procedure TMyClass.myProcedure();
var
    i: integer;
begin
    for i := 1 to 5 do
    begin
        writeln(i);
        i := fDemo1.getFunction();
    end;
end;

procedure TMyClass.setSomething();
begin
    setSomething;
end;

{******* STAND ALONE GLOBAL FUNCTIONS ****}
procedure StandAloneProcedure(x, y: real; z: boolean; var ppp);
var
    x, y: integer;
begin
    x := 5;
    y := 7;
    while x < 500 do
    begin
        if(x > 7) then y := 2
        else y := 0;
    end;

end;

function StandAloneFunction(var x: TdDemo): integer;
begin
    StandAloneProcedure(1,1);
end;

end.
