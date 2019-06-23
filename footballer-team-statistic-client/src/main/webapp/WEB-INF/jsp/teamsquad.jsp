<%--
  Created by IntelliJ IDEA.
  User: Admin
  Date: 06.06.2019
  Time: 17:33
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.1.3/css/bootstrap.min.css" integrity="sha384-MCw98/SFnGE8fJT3GXwEOngsV7Zt27NXFoaoApmYm81iuXoPkFOJwJ8ERdknLPMO" crossorigin="anonymous">
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <title>Team ${team.id} squad</title>
</head>
<jsp:include page="menu.jsp" />
<body>

<div class="col-10 offset-1">

    <div class="center-block headers-region">
        <h1 class="header-panel">Team ${team.name} (${team.city})</h1>
    </div>

    <table class="table table-striped">
        <thead>
        <tr>
            <th scope="col">Id</th>
            <th scope="col">Name</th>
            <th scope="col">Age</th>
            <th scope="col">Actions</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach  items="${footballers}" var="e">
            <tr>
                <th scope="row">${e.id}</th>
                <td>${e.firstname} ${e.surname}</td>
                <td>${e.age}</td>
                <td>
                    <div>
                        <a type="button" class="btn btn-danger" href="/teams/${team.id}/footballers/remove/${e.id}" >Remove</a>
                    </div>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
    <!--<button type="button" class="button btn btn-warning center-block">To list</button>-->
    <a type="button" href="/teams/all" class="btn btn-warning">To list</a>

</div>
<script src="https://code.jquery.com/jquery-3.3.1.slim.min.js" integrity="sha384-q8i/X+965DzO0rT7abK41JStQIAqVgRVzpbzo5smXKp4YfRvH+8abtTE1Pi6jizo" crossorigin="anonymous"></script><script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.3/umd/popper.min.js" integrity="sha384-ZMP7rVo3mIykV+2+9J3UJ46jBk0WLaUAdn689aCwoqbBJiSnjAK/l8WvCWPIPm49" crossorigin="anonymous"></script>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.1.3/js/bootstrap.min.js" integrity="sha384-ChfqqxuZUCnJSK3+MXmPNIyE6ZbWh2IMqE241rYiqJxyMiZ6OW/JmZQ5stwEULTy" crossorigin="anonymous"></script>
</body>
</html>
