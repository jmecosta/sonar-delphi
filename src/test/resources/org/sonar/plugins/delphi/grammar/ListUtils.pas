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

unit ListUtils;

interface

{$I Elotech.inc}

{$IFDEF Delphi7}
uses Classes;
{$ELSE}
{$ENDIF}
type
    TListUtils = class
    public
        class procedure Normal();
        class procedure AddAll1(AList: TList<string>; const AValue: string; ADelimiter: Char = ';'); overload;
        class procedure AddAll2(AList: TList<Integer>; const AValue: string; ADelimiter: Char = ';'); overload;
        class procedure AddAll3<T>
            (AList: TList<T>; const AValue: string; AConverter: TFunc<String,T>; ADelimiter: Char = ';'); overload;
    end;

implementation

{ TListUtils }

class procedure TListUtils.AddAll1(AList: TList<Integer>; const AValue: string; ADelimiter: Char);
begin
    AddAll3<Integer>(AList, AValue, function (Value: String): Integer
    begin
        Result := StrToInt(Value);
    end,
        ADelimiter);
end;

class procedure TListUtils.AddAll2(AList: TList<string>; const AValue: string; ADelimiter: Char);
begin
    AddAll3<String>(AList, AValue, function (Value: String): string
    begin
        Result := Value;
    end,
        ADelimiter);
end;

class procedure TListUtils.AddAll3<T>
    (AList: TList<T>; const AValue: string; AConverter: TFunc<String,T>; ADelimiter: Char);
var
    vList: TStringList;
    vItem: String;
begin
    Assert(Assigned(AConverter), 'Converter n�o definido');

    vList := TStringList.Create;
    try
        vList.Delimiter := ADelimiter;
        vList.DelimitedText := AValue;

        for vItem in vList do
        begin
            AList.Add(AConverter(vItem))
        end;
    finally
        vList.Free;
    end;
end;

class procedure TListUtils.Normal;
begin
    //Comment
end;

end.
