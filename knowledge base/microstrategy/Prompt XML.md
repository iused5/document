# MicroStrategy Prompt XML
## promptXML 형식
```xml
<rsl>
    {PROMPT}
</rsl>
```
* {PROMPT}는 다음의 프롬프트 항목들로 구성된다.

### EnumWebPromptType.WebPromptTypeConstant // 상수프롬프트 (int 1)
```xml
<!-- 형식: -->
<pa pt="4" pin="0" did="{PROMPT_ID}" tp="10">{VALUE}</pa>
    
<!-- 예시: -->
<pa pt="4" pin="0" did="ACF66BB94545BA2DD025A4B0247C309D" tp="10">10/10/2009</pa>
```

### EnumWebPromptType.WebPromptTypeExpression // 계층구조프롬프트 (int 3)
```xml
<!-- 형식: -->
<pa pt="8" pin="0" did="{PROMPT_ID}" tp="10">
    <exp>
        <nd et="5" nt="4" dmt="1" ddt="-1">
            <nd et="1" nt="5" dmt="1" ddt="-1">
                <at did="{DIMENSION_ID}" tp="12"/>
            </nd>
            <nd et="1" nt="2" dmt="1" ddt="-1">
                <mi>
                    <es>
                        <at did="{DIMENSION_ID}" tp="12"/>
                        {{!repeat}
                        <e emt="1" ei="{ELEMENT_ID}" art="1" disp_n="{ELEMENT_CAPTION}"/>
                        }
                    </es>
                </mi>
            </nd>
            <op fnt="22"/>
        </nd>
    </exp>
</pa>
    
<!-- 예시: -->
<pa pt="8" pin="0" did="2AD96A8C4190EEA9CB32B2BE2CEF4C81" tp="10">
    <exp>
        <nd et="5" nt="4" dmt="1" ddt="-1">
            <nd et="1" nt="5" dmt="1" ddt="-1">
                <at did="8D679D3811D3E4981000E787EC6DE8A4" tp="12"/>
            </nd>
            <nd et="1" nt="2" dmt="1" ddt="-1">
                <mi>
                    <es>
                        <at did="8D679D3811D3E4981000E787EC6DE8A4" tp="12"/>
                        <e emt="1" ei="8D679D3811D3E4981000E787EC6DE8A4:1" art="1" disp_n="USA"/>
                    </es>
                </mi>
            </nd>
            <op fnt="22"/>
        </nd>
    </exp>
</pa>
    
<!-- 미선택시: -->
<pa pt="8" pin="0" did="034E0D5E4B23608C945FBEB7D9E7E90C" tp="10">
    <exp/>
</pa>
    
<!-- 형식: 여러레벨의 계층 선택시 -->
<pa pt="8" pin="0" did="{PROMPT_ID}" tp="10">
    <exp>
        <nd et="14" nt="4" dmt="1" ddt="-1">
            {{!repeat}  
            <nd et="5" nt="4" dmt="1" ddt="-1">
                <nd et="1" nt="5" dmt="1" ddt="-1">
                    <at did="{DIMENSION_ID}" tp="12"/>
                </nd>
                <nd et="1" nt="2" dmt="1" ddt="-1">
                    <mi>
                        <es>
                            <at did="{DIMENSION_ID}" tp="12"/>
                            {{!repeat}
                            <e emt="1" ei="{ELEMENT_ID}" art="1" disp_n="{ELEMENT_CAPTION}"/>
                            }
                        </es>
                    </mi>
                </nd>
                <op fnt="22"/>
            </nd>
            }
            <op fnt="19"/>
        </nd>
    </exp>
</pa>
    
<!-- 예시: -->
<pa pt="8" pin="0" did="034E0D5E4B23608C945FBEB7D9E7E90C" tp="10">
    <exp>
        <nd et="14" nt="4" dmt="1" ddt="-1">
            <nd et="5" nt="4" dmt="1" ddt="-1">
                <nd et="1" nt="5" dmt="1" ddt="-1">
                    <at did="8D679D5111D3E4981000E787EC6DE8A4" tp="12"/>
                </nd>
                <nd et="1" nt="2" dmt="1" ddt="-1">
                    <mi>
                        <es>
                            <at did="8D679D5111D3E4981000E787EC6DE8A4" tp="12"/>
                            <e emt="1" ei="8D679D5111D3E4981000E787EC6DE8A4:2010" art="1" disp_n="2010"/>
                        </es>
                    </mi>
                </nd>
                <op fnt="22"/>
            </nd>
            <nd et="5" nt="4" dmt="1" ddt="-1">
                <nd et="1" nt="5" dmt="1" ddt="-1">
                    <at did="8D679D4511D3E4981000E787EC6DE8A4" tp="12"/>
                </nd>
                <nd et="1" nt="2" dmt="1" ddt="-1">
                    <mi>
                        <es>
                            <at did="8D679D4511D3E4981000E787EC6DE8A4" tp="12"/>
                            <e emt="1" ei="8D679D4511D3E4981000E787EC6DE8A4:1" art="1" disp_n="1월"/>
                        </es>
                    </mi>
                </nd>
                <op fnt="22"/>
            </nd>
            <nd et="5" nt="4" dmt="1" ddt="-1">
                <nd et="1" nt="5" dmt="1" ddt="-1">
                    <at did="8D679D4A11D3E4981000E787EC6DE8A4" tp="12"/>
                </nd>
                <nd et="1" nt="2" dmt="1" ddt="-1">
                    <mi>
                        <es>
                            <at did="8D679D4A11D3E4981000E787EC6DE8A4" tp="12"/>
                            <e emt="1" ei="8D679D4A11D3E4981000E787EC6DE8A4:20111" art="1" disp_n="2011제일분기"/>
                        </es>
                    </mi>
                </nd>
                <op fnt="22"/>
            </nd>
            <op fnt="19"/>
        </nd>
    </exp>
</pa>
<!-- 포함, 예외 처리 및 AND, OR 연산을 고려하지 않은 상태 -->
```

### EnumWebPromptType.WebPromptTypeElements // (int 2)
```xml
<!-- 형식: -->
<pa pt="7" pin="0" did="{PROMPT_ID}" tp="10">
    <mi>
        <es>
            <at did="{DIMENSION_ID}" tp="12"/>
            {{!report}
            <e emt="1" ei="{ELEMENT_ID}" art="1" disp_n="{ELEMENT_NAME}"/>            
            }
        </es>
    </mi>
</pa>
    
<!-- 예시: -->
<pa pt="7" pin="0" did="F7B2581B4ED0B7426F68D892D40C433C" tp="10">
    <mi>
        <es>
            <at did="8D679D3711D3E4981000E787EC6DE8A4" tp="12"/>
            <e emt="1" ei="8D679D3711D3E4981000E787EC6DE8A4:4" art="1" disp_n="Music"/>
            <e emt="1" ei="8D679D3711D3E4981000E787EC6DE8A4:3" art="1" disp_n="Movies"/>
            <e emt="1" ei="8D679D3711D3E4981000E787EC6DE8A4:2" art="1" disp_n="Electronics"/>
            <e emt="1" ei="8D679D3711D3E4981000E787EC6DE8A4:1" art="1" disp_n="Books"/>
        </es>
    </mi>
</pa>
    
<!-- 미선택시 -->
<pa pt="7" pin="0" did="51B8DB554346F27B715C97A2ADF654FA" tp="10">
    <mi>
        <es/>
    </mi>
</pa>
```

### EnumWebPromptType.WebPromptTypeObjects // (int 4)
```xml
<!-- 형식: -->
<pa pt="6" pin="0" did="{PROMPT_ID}" tp="10">
    <mi>
        <fct>
            {{!REPEAT}
            <at did="{ELEMENT_ID}" tp="4"/>
            }
        </fct>
    </mi>
</pa>
<!--     
    <fct>는 <fct qsr="0" fcn="0" cc="{{!선택된 엘리먼트 수}}" sto="1" pfc="0"
     pcc="{{!선택된 엘리먼트 수}}"> 가능
-->     
     
<!-- 예시: -->
<pa pt="6" pin="0" did="908ADE6949DC2CF7C5ABDE942935C851" tp="10">
    <mi>
        <fct>
            <at did="4C05177011D3E877C000B3B2D86C964F" tp="4"/>
            <at did="7FD5B69611D5AC76C000D98A4CC5F24F" tp="4"/>
        </fct>
    </mi>
</pa>
<!-- 미선택시 -->
<pa pt="6" pin="0" did="44AC0970463C157A8B4CA0A2E062BBD7" tp="10">
    <mi>
        <fct qsr="0" fcn="0" cc="0" sto="1" pfc="0" pcc="0"/>
    </mi>
</pa>
```