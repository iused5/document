# VBA로 Module exporting
```vbnet
Public Sub testExportCode()
    Dim pt As String
    Dim ed As VBIDE.VBE
    Dim pj As VBIDE.VBProject
    Dim cp As VBIDE.VBComponent
    Dim cm As VBIDE.CodeModule
    Dim cd As String
    
    Dim fs, f, ts
    
    pt = "c:\temp\"
    
    Set fs = CreateObject("Scripting.FileSystemObject")
    
    For Each cp In Application.VBE.ActiveVBProject.VBComponents
        With cp
            Debug.Print .Name
            fs.createTextFile (pt & .Name & ".exp")
            Set f = fs.getfile(pt & .Name & ".exp")
            Set ts = f.OpenAsTextStream(2)
            Set cm = cp.CodeModule
            If (cm.CountOfLines > 0) Then
                ts.write cm.Lines(1, cm.CountOfLines)
            End If
            ts.Close
        End With
    Next
        
    Set ts = Nothing
    Set f = Nothing
    Set fs = Nothing
    
    Set cm = Nothing
    Set cp = Nothing
    Set pj = Nothing
    Set ed = Nothing
End Sub
```