# VBA Reference 자동 등록
```vbnet
Option Compare Database
Option Explicit
Option Base 1
    
Const LIB_1 = "VBScript_RegExp_55"
Const LIB_2 = ""
    
Function testRegExp()
    Dim r As Variant, i As Integer, s As String
    Dim pt As String, tg As String
    
    pt = "<a>(?:(?!<a>).)*?</a>"
    'pt = "(?<=<a>)(?:(?!<a>).)*?(?=</a>)" ' 에러 발생
    tg = "<a><a>AA</a><a>ABC</a></a><a>BFGB</a><a>XFVB</a>"
    
    r = getPStrings(pt, tg)
    
    For i = LBound(r) To UBound(r)
        s = r(i)
        s = replacePString("</{0,1}a>", "", s)
        
        Debug.Print "result " & i; " is [" & s & "]"
    Next
End Function
    
Function replacePString(p As String, rep As String, tgt As String) As String
On Error GoTo ErrOnReplacePString
    Dim re As New RegExp
    
    With re
        .Pattern = p
        .Global = True
        
        replacePString = .Replace(tgt, rep)
    End With
    Exit Function
    
ErrOnReplacePString:
    replacePString = ""
End Function
    
Function getPStrings(p As String, tgt As String) As Variant
On Error GoTo ErrOngetPStrings
    Dim re As New RegExp
    Dim result() As String
    Dim m, matches, i As Integer
            
    With re
        .Pattern = p
        .Global = True
        
        Set matches = .Execute(tgt)
    End With
    
    ReDim result(1 To matches.Count) As String
    
    For i = 1 To matches.Count
        result(i) = matches(i - 1).Value
    Next
    
    getPStrings = result
    Exit Function
    
ErrOngetPStrings:
    getPStrings = ""
End Function
    
Public Sub validateReferences()
    If Not hasReference(LIB_1) Then
        MsgBox "select lib"
    Else
        MsgBox "OK"
    End If
End Sub
    
Public Function hasReference(rnm As String) As Boolean
    Dim r As Reference
    
On Error GoTo ErrOnHasReference
    
    For Each r In Application.References
        If (r.Name = rnm) Then
            hasReference = Not r.IsBroken
            Exit Function
        End If
    Next
    
    hasReference = False
    Exit Function
    
ErrOnHasReference:
    MsgBox "Error on function hasReference" & Chr(10) & "[" & Err.Description & "]"
    
    hasReference = False
End Function
    
Sub testSelectSingleFile()
    Dim fnm As String
    
    fnm = SelectSingleFile("", "c:/")
    Debug.Print "[" & fnm & "]"
End Sub
    
Public Function SelectSingleFile(Optional title As String = "", Optional path As String, Optional filter As String) As String
    Dim f As FileDialog, files As Variant
    
On Error GoTo ErrOnSelectSingleFile
    
    Set f = Application.FileDialog(msoFileDialogOpen)
    With f
        .allowMultiSelect = False
        If (title <> "") Then
            .title = "파일을 선택하세요"
        End If
        If (path <> "") Then
            .initialFileName = path
        End If
        If (filter <> "") Then
            .filters.Add "Text", "*.txt", 1
        End If
        
        If (.show = -1) Then
            For Each files In .SelectedItems
                SelectSingleFile = Trim(files)
                Exit For
            Next files
        End If
    End With
    Set f = Nothing
    
    Exit Function
    
ErrOnSelectSingleFile:
    Set f = Nothing
    
    SelectSingleFile = ""
    Exit Function
End Function
```