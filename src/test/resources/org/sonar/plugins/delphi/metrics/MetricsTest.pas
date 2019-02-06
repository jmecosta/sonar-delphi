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

//comment line 1
//comment line 2

type
    TfDemo = class(TForm, TMultiCheckbox)
        bShowTracker: TButton;
        procedure bShowTrackerClick(Sender, Sender2: TObject; x: Integer);
        function fClassMethod(var x, z: real): integer;
    private
        //comment line 3
        private1var: string;
    public
        public1var, pub2var: integer;
    protected
        protected1var: real;
    end;

var
    fDemo1, fDemo2: TfDemo;
    bigInt: Integer;

implementation

{ comment line 

6 }    //causing trouble //trouble //trouble

(* comment 
line 8 *)  { ddd } {ffff } // fff

procedure StandAloneProcedure(x, y: real);
begin
    x := 5;
    y := 7;
    while x < 5 do
    begin
        if(x > 7) then y := 2
        else y := 0;
    end;

end;

{** doc **}
function StandAloneFunction(var x: TdDemo): integer;
begin

end;

{*
Assumes that all rows  have the same number of columns
*}
function TfDemo.FunctionName1(SenderXXX: TObject): integer;
begin

end;

{** function 
documentation 
2 *******}

procedure TfDemo.ProcedureName1(SenderYYY: TObject);
var
    str: string;
begin
    str := 'sample string';
end;

end.