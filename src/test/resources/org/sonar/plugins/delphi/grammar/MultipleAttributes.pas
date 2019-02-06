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

unit ChangeReasonTable;

interface

type
    TChangeReasonRecordSet<GenericRecord: TDatabaseRecord, constructor> = class(TDatabaseRecordSet<GenericRecord>)
    protected
        function BuildQueryForOpCodeDescription
            (OpType: TOpType; SystemType: TSystemInterface; const Identifier, FTOpCode: String): ISQLBuilder;
        function BuildQueryForOpTypeFliteTrackOnly(OpType: TOpType; FliteTrackOnly: Boolean): ISQLBuilder;
        function GetTableName(): string; override;
    public
        function Open(OpType: TOpType; FliteTrackOnly: Boolean): Boolean; overload;
        function Open(OpType: TOpType; SystemType: TSystemInterface; const Identifier, FTOpCode: String): Boolean;
        overload;
    end;

    [DatabaseIndex([diUnique], fldRecordStatus, fldCompanyCode, fldOpType, fldSystemType, fldIdentifier, fldFTOpCode,
fldHistoryTag)]
    [
    DatabaseIndex([], fldRecordStatus, fldCompanyCode, fldOpType, fldSystemType, fldFTOpCode, fldHistoryTag, fldRecordId
    )]
    [
    DatabaseIndex([], fldRecordStatus, fldCompanyCode, fldOpType, fldFTOpCode, fldSystemType, fldHistoryTag, fldRecordId
    )]
    TChangeReasonTable = class(TChangeReasonRecordSet<TChangeReasonRecord>);

implementation

{ TChangeReasonRecordSet<GenericRecord> }

function TChangeReasonRecordSet<GenericRecord>.BuildQueryForOpCodeDescription
    (OpType: TOpType; SystemType: TSystemInterface;
        const Identifier, FTOpCode: String): ISQLBuilder;
var
    SQLStatement: ISQLStatement;
begin
    Result := GetSQLBuilder();
    SQLStatement := Result.Field(fldRecordStatus).Equals(RecordActive)._And.Field(fldCompanyCode).Equals(CompanyCode)
._And.Field(fldOpType).Equals(Ord(OpType))._And.Field(fldSystemType).Equals(SystemType)
._And.Field(fldIdentifier).Equals(UpperCase(Identifier))._And.Field(fldFTOpCode).Equals(UpperCase(FTOpCode));
end;

function TChangeReasonRecordSet<GenericRecord>.BuildQueryForOpTypeFliteTrackOnly
    (OpType: TOpType; FliteTrackOnly: Boolean): ISQLBuilder;
var
    SQLStatement: ISQLStatement;
begin
    Result := GetSQLBuilder();
    SQLStatement := Result.Field(fldRecordStatus).Equals(RecordActive)._And.Field(fldCompanyCode).Equals(CompanyCode)
._And.Field(fldOpType).Equals(Ord(OpType));
    if not FliteTrackOnly then
    begin
        SQLStatement.OrderBy([fldRecordStatus, fldOpType]);
    end
    else
    begin
        SQLStatement := SQLStatement._And.Field(fldSystemType).Equals(0);
        SQLStatement.OrderBy([fldRecordStatus, fldOpType, fldSystemType, fldIdentifier]);
    end;
end;

function TChangeReasonRecordSet<GenericRecord>.GetTableName: string;
begin
    Result := tblChangeReason;
end;

function TChangeReasonRecordSet<GenericRecord>.Open(OpType: TOpType; SystemType: TSystemInterface; const Identifier,
    FTOpCode: String): Boolean;
var
    SQLBuilder: ISQLBuilder;
begin
    SQLBuilder := BuildQueryForOpCodeDescription(OpType, SystemType, Identifier, FTOpCode);
    Result := inherited Open(SQLBuilder) and HasRecords();
end;

function TChangeReasonRecordSet<GenericRecord>.Open(OpType: TOpType; FliteTrackOnly: Boolean): Boolean;
var
    SQLBuilder: ISQLBuilder;
begin
    SQLBuilder := BuildQueryForOpTypeFliteTrackOnly(OpType, FliteTrackOnly);
    Result := inherited Open(SQLBuilder) and HasRecords();
end;

initialization
    TChangeReasonTable.RegisterTable(tblChangeReason);

end.
