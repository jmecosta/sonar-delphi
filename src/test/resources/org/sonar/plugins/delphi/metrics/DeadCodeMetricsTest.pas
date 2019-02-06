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

unit DeadCodeUnit;

interface

type
    myInterface = Interface(IInterface)
        procedure foo;
        function bar: integer;
    end;

    myClass = class(myInterface)
    public
        procedure foo;                //no violation, interface implementation
        procedure UnusedProcedure;    //violation
        procedure UsedProcedure;    //no violation, procedure used
        function bar: integer;        //no violation, interface implementation
        procedure vii; virtual;    //no violation, virtual procedure
        procedure viiii; override;    //no violation, virtual procedure
        procedure readFunc;            //no violation, used in property
        procedure writeFunc;        //no violation, used in property
        procedure UnusedToo;        //violation
        procedure WMPaint(var Message : TWMPaint); message WM_PAINT;    //no violation, because message
    published
        property isSth : Boolean read readFunc write writeFunc;
    end;

implementation

procedure myClass.foo;
begin
end;

function myClass.bar: integer;
begin
end;

procedure myClass.UnusedProcedure;
begin
    UsedProcedure;
end;

procedure myClass.UsedProcedure;
begin
end;

procedure myClass.WMPaint(var Message : TWMPaint);
begin

end;

end.