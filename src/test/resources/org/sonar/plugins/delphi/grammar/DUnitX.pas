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

unit DUnitX.AutoDetect.Console;

{ Copyright Unicode Support}

interface

(** Code snippets from https://github.com/VSoftTechnologies/DUnitX **)

type
    TValueHelper = record helper for TValue
    private
    end;

type
    TTestLocalMethod = TProc;

    TTestMethod = procedure of object;

    Assert = class
        //Implements could be used as identifier
        class function Implements<T : IInterface>(value : IInterface; const message : string = '' ) : T;
    end;

    IMyInterface = interface
    ['{9B59FF6D-7812-46A6-AFBD-89560AA639DB}']
    end;

    TDUnitXEnumerable = class(TInterfacedObject, IEnumerable)
    protected
        //function IEnumerable.GetEnumerator = GetNonGenEnumerator;
        //function GetNonGenEnumerator : IEnumerator; virtual; abstract;
    end;

    TDUnitXTestFixture = class(TWeakReferencedObject, ITestFixture,ITestFixtureInfo)
//    function ITestFixtureInfo.GetTests = ITestFixtureInfo_GetTests;
    end;

implementation

procedure testUnicode;
var
    vString: string;
    vTest: string;
begin
    //vString := vTest;
    vString := #1234;
    vString := 'aaaa';
    vString := '©';
end;

end.
