<%@ page import="com.caseo.domain.model.ObjectModel" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" %>

<ul class="menu">

  <li><a href="${pageContext.request.contextPath}/">Личный кабинет</a></li>

  <li>
    <a href="${pageContext.request.contextPath}/organization?mode=view&orgId=${sessionScope.orgId}">
      Организация
    </a>
  </li>

  <%
    @SuppressWarnings("unchecked")
    List<ObjectModel> sidebarObjects =
            (List<ObjectModel>) request.getAttribute("sidebarObjects");

    String mode = (String) request.getAttribute("mode");
    String orgId = (String) session.getAttribute("orgId");
  %>

  <li class="sidebar-section">

    <div class="sidebar-title" onclick="createNewObject()">
      Объекты
    </div>

    <ul class="sidebar-list">

      <% if (sidebarObjects != null) {
        for (ObjectModel obj : sidebarObjects) { %>

      <li>
        <a href="objects?mode=<%=mode%>&orgId=<%=orgId%>&id=<%=obj.getId()%>">

          <%= obj.getObjectShortName() != null && !obj.getObjectShortName().isEmpty()
                  ? obj.getObjectShortName() : "Новый объект"%>

        </a>
      </li>

      <%   }
      } %>

    </ul>

  </li>

</ul>