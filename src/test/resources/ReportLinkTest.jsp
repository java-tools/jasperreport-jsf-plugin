<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://jasperforge.org/jasperreports/jsf" prefix="jr" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" 
  "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
	
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<title>Inline Report Test</title>
</head>

<body>
<f:view>
<jr:report id="reportLink"
	report="/WEB-INF/report/Factura.jasper"
	subreportDir="/WEB-INF/report/"
>
	<h:outputText>Report</h:outputText>
</jr:report>
</f:view>
</body>
</html>