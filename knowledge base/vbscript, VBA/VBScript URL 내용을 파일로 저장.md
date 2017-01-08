# VBScript URL 내용을 파일로 저장
```vbnet
On Error Resume Next
    
Const URL = "http://127.0.0.1:8080/microstrategy/servlet/mstrWeb"
Const QRY = "Uid=administrator&Pwd=xxx&Server=xxx&Port=0&project=xxx&evt=2048001&src=mstrWeb.2048001&visualizationMode=0&documentID={ID}&promptsAnswerXML={PROMPT}"
Const LOG = "c:\call_history\log\"
    
Dim arr
    
arr =   Array( _
                Array("EEEEE", "%3Crsl%3E%3Cpa+pt%3D%277%27+pin%3D%270%27+did%3D%278F257A4C44886327933FE39DDCF00E6B%27+tp%3D%2710%27%3E%3Cmi%3E%3Ces%3E%3Cat+did%3D%277A74D0E448715138B6289F8BAE93FFCB%27+tp%3D%2712%27%2F%3E%3Ce+emt%3D%271%27+ei%3D%277A74D0E448715138B6289F8BAE93FFCB%3A201002%27+art%3D%271%27+disp_n%3D%272010-02%27%2F%3E%3C%2Fes%3E%3C%2Fmi%3E%3C%2Fpa%3E%3C%2Frsl%3E", Year(Date()) & Right("0" & Month(Date()) - 1, 2), Year(Date()) & "-" & Right("0" & Month(Date()) - 1, 2)), _
                Array("EEEEE", "%3Crsl%3E%3Cpa+pt%3D%277%27+pin%3D%270%27+did%3D%278F257A4C44886327933FE39DDCF00E6B%27+tp%3D%2710%27%3E%3Cmi%3E%3Ces%3E%3Cat+did%3D%277A74D0E448715138B6289F8BAE93FFCB%27+tp%3D%2712%27%2F%3E%3Ce+emt%3D%271%27+ei%3D%277A74D0E448715138B6289F8BAE93FFCB%3A201002%27+art%3D%271%27+disp_n%3D%272010-02%27%2F%3E%3C%2Fes%3E%3C%2Fmi%3E%3C%2Fpa%3E%3C%2Frsl%3E", Year(Date()) & Right("0" & Month(Date()) - 1, 2), Year(Date()) & "-" & Right("0" & Month(Date()) - 1, 2)), _
                Array("EEEEE", "%3Crsl%3E%3Cpa+pt%3D%277%27+pin%3D%270%27+did%3D%278F257A4C44886327933FE39DDCF00E6B%27+tp%3D%2710%27%3E%3Cmi%3E%3Ces%3E%3Cat+did%3D%277A74D0E448715138B6289F8BAE93FFCB%27+tp%3D%2712%27%2F%3E%3Ce+emt%3D%271%27+ei%3D%277A74D0E448715138B6289F8BAE93FFCB%3A201002%27+art%3D%271%27+disp_n%3D%272010-02%27%2F%3E%3C%2Fes%3E%3C%2Fmi%3E%3C%2Fpa%3E%3C%2Frsl%3E", Year(Date()) & Right("0" & Month(Date()) - 1, 2), Year(Date()) & "-" & Right("0" & Month(Date()) - 1, 2)), _
                Array("EEEEE", "%3Crsl%3E%3Cpa+pt%3D%277%27+pin%3D%270%27+did%3D%278F257A4C44886327933FE39DDCF00E6B%27+tp%3D%2710%27%3E%3Cmi%3E%3Ces%3E%3Cat+did%3D%277A74D0E448715138B6289F8BAE93FFCB%27+tp%3D%2712%27%2F%3E%3Ce+emt%3D%271%27+ei%3D%277A74D0E448715138B6289F8BAE93FFCB%3A201002%27+art%3D%271%27+disp_n%3D%272010-02%27%2F%3E%3C%2Fes%3E%3C%2Fmi%3E%3C%2Fpa%3E%3C%2Frsl%3E", Year(Date()) & Right("0" & Month(Date()) - 1, 2), Year(Date()) & "-" & Right("0" & Month(Date()) - 1, 2)) _
                )
    
Dim xmlhttp
Set xmlhttp = CreateObject("Microsoft.XMLHTTP")
    
Dim urlStr, parStr, fso, i
Set fso = CreateObject("Scripting.FileSystemObject")
    
Dim logFile, beginTime, endTime
    
If not fso.FileExists(LOG & "runtime.log") then
    Set logFile = fso.CreateTextFile(LOG & "runtime.log", True)
    logFile.Close()
    Set logFile = nothing
End If
    
Set logFile = fso.OpenTextFile(LOG & "runtime.log", 8, True)
    
for i = 0 to UBound(arr)
    prmStr = Replace(arr(i)(1), "{VALUE1}", arr(i)(2))
    prmStr = Replace(prmStr, "{VALUE2}", arr(i)(3))
    
    parStr = Replace(QRY, "{ID}", arr(i)(0))
    parStr = Replace(parStr, "{PROMPT}", prmStr)
    
    beginTime = Now()
    
    xmlhttp.open "POST", URL, false
    xmlhttp.setRequestHeader "Content-Type", "application/x-www-form-urlencoded"
    xmlhttp.send parStr
    
    Dim file
    Set file = fso.CreateTextFile(LOG & "http_" & Right("0" & i, 2) & ".html", True)
    file.Write (parStr)
    file.Write (xmlhttp.responseText)
    file.Close
    Set file = nothing
    
    endTime = now()
    
    logFile.WriteLine (i & ",수행시간," & beginTime & "," & endTime)
next
    
logFile.close
    
Set logFile = nothing
Set fso = nothing
Set xmlhttp = nothing
```