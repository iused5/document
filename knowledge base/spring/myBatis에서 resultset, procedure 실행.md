# Spring - myBatis에서 resultset, procedure 실행
## Resultset
```java
simpleBizDwDao.listJdbcSql(lastUploadDataStateSql,
    new AbstractResultSetCallBack() {
        @Override
        public void callback(ResultSet rs) {
            try {
                ResultSetMetaData rsmd = rs.getMetaData();
                for (int i = 1; i <= rsmd.getColumnCount(); i++) {
                    headers.add(rsmd.getColumnLabel(i));
                }
                while (rs.next()) {
                    Map<String, Object> map = new HashMap<String, Object>();
                    for (String header : headers) { map.put(header, rs.getObject(header)); }
                    data.add(map);
                }
            } catch (SQLException e) {
                headers.clear();;
                data.clear();
            }
        }
    });
    
// transaction이 필요하다면 별도 처리 필요
public ResultSet listJdbcSql(String sql, AbstractResultSetCallBack callback) {
    Connection cn = null;
    Statement st = null;
    ResultSet rs = null;
    try {
        cn = getSqlSession().getConfiguration().getEnvironment().getDataSource().getConnection();
        st = cn.createStatement();
        rs = st.executeQuery(sql);
        callback.callback(rs);
    } catch (SQLException e) {
        e.printStackTrace();
    } finally {
        if (rs != null) try { rs.close(); } catch (Exception e) { e.printStackTrace(); }
        if (st != null) try { st.close(); } catch (Exception e) { e.printStackTrace(); }
        if (cn != null) try { cn.close(); } catch (Exception e) { e.printStackTrace(); }
    }
    
    return rs;
}
```

## Oracle Procedure
```xml
<resultMap id="mstrMenuResultMap" type="HashMap">
    <result property="tp1" column="TP1" jdbcType="INTEGER" javaType="java.lang.Integer"/>
    <result property="stp1" column="STP1" jdbcType="INTEGER" javaType="java.lang.Integer"/>
    <result property="id1" column="ID1" jdbcType="VARCHAR" javaType="java.lang.String"/>
    <result property="nm1" column="NM1" jdbcType="VARCHAR" javaType="java.lang.String"/>
    <result property="gb1" column="GB1" jdbcType="VARCHAR" javaType="java.lang.String"/>
    ...
</resultMap>
    
<select id="mstr-menu-list" parameterType="Map" statementType="CALLABLE">
{call MSTR_META.UP_GET_REPORT_LIST(
    #{V_USERID, mode=IN, jdbcType=VARCHAR, javaType=String},
    #{V_LOCALE, mode=IN, jdbcType=INTEGER, javaType=Integer},
    #{V_RS, mode=OUT, jdbcType=CURSOR, javaType=java.sql.ResultSet, resultMap=mstrMenuResultMap}
)}
</select>
```

```java    
@Override
public List<Map<String, Object>> mstrMenuList(Map<String, Object> param) {
    simpleBizMetaDao.list("main.mstr-menu-list", param);
    List<Map<String, Object>> list = (List<Map<String, Object>>)param.get("V_RS");      
    return list;
}
    
@RequestMapping(value = "/mstr-menu_list.json", method = {RequestMethod.GET, RequestMethod.POST})
@ResponseBody
public List<Map<String, Object>> mstrMenulist(HttpServletRequest request) {
    Map<String, Object> param = new HashMap<String, Object>();
    param.put("V_USERID", SessionInfoSupport.getCurrentUser());
    param.put("V_LOCALE", HttpUtil.getLcidNum(request));
    
    return mainService.mstrMenuList(param);
}
```