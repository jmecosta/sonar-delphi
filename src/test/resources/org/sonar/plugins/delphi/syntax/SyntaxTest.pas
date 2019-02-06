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

unit TestUnit;

{$deFIne XYZ}

interface

//excluded comment

(* nasty
	{ comment 
		//test {$include error.inc}
	} *)


implementation

const
{$include info.inc}
{$I info.inc}

{$ifdef THERE_ARE_ERRORS}
        THERE ARE ERRORS WITH $DEFINE IN A STRING
{$endif}

procedure TestProcedure();
var
    String: str, str2, unicodeString;
begin
    { another
    excluded block
    comment }

    str = 'string to be {excluded}';    //comment
    str2 = 'another string';
    string := '_¢Ã»Ã_''_¢Â_''_¢ Ã_''_¯Ã°Ã_';
end;

begin
end.