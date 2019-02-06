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

//test
{ test }
(* test *)

type
    TfDemo = class(TForm)
        bShowTracker: TButton;
        procedure bShowTrackerClick(Sender: TObject);
    private
        //test
    public
        {test}
    end;

type
    TfDemoSecond = class (TForm)
        bShowTracker: TButton;
        procedure bShowTrackerClick(Sender: TObject);
    private

    public

    end;

var
    fDemo: TfDemo;

implementation

procedure TfDemo.bShowTrackerClick(Sender: TObject);
begin
    (* long
    multiline
    comment
    *)

    string = "sample string";

    for i:=0 to 100 do
    begin
        a := b;
        b := c;
        c := d;
        a := b;
        if a < b then
            c := b;
    end;

    for i:=0 to 100 do
    begin
        a := b;
        b := c;
        c := d;
        a := b;
        if a < b then
            c := b;
    end;

end;

end.