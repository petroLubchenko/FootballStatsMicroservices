<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>

<nav class="navbar navbar-expand-lg">
    <a class="navbar-brand" href="/main">Portal of the football</a>
    <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarNavAltMarkup" aria-controls="navbarNavAltMarkup" aria-expanded="false" aria-label="Toggle navigation">
        <span class="navbar-toggler-icon"></span>
    </button>
    <div class="collapse navbar-collapse" id="navbarNavAltMarkup">
        <div class="navbar-nav">
            <security:authorize access="hasRole('ADMIN')">
                <a class="nav-item nav-link" href="/admin">Users</a>
                <a class="nav-item nav-link" href="/admin/logs">Logs</a>
            </security:authorize>
            <a class="nav-item nav-link" href="/footballers/all">Footballers</a>
            <a class="nav-item nav-link" href="/teams/all">Teams</a>
            <a class="nav-item nav-link" href="/championships/">Championships</a>
            <div style="color: #343a40;
    min-width: 150%;">.</div>
            <div class="nav-item navbar-text" align="right">${name}</div>
            <a class="nav-item nav-link" href="/logout" style="alignment: right; horiz-align: right">Logout</a>
        </div>
    </div>
</nav>