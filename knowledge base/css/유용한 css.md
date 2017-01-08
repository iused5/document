# 유용한 css
table의 사이즈가 고정되지 않은 td에 ellipsis 표시
```css
table {
    table-layout: fixed;
}

td {
    text-overflow: ellipsis; 
    white-space: nowrap; 
    overflow: hidden;
}
```