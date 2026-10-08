<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<c:if test="${not empty errorMessage}">
    <div class="alert alert-danger" role="alert">
        <span>⚠️</span>
        <div>${errorMessage}</div>
    </div>
</c:if>

<c:if test="${not empty successMessage}">
    <div class="alert alert-success" role="alert">
        <span>✅</span>
        <div>${successMessage}</div>
    </div>
</c:if>

<c:if test="${not empty param.error}">
    <div class="alert alert-danger" role="alert">
        <span>⚠️</span>
        <div>${param.error}</div>
    </div>
</c:if>

<c:if test="${not empty param.success}">
    <div class="alert alert-success" role="alert">
        <span>✅</span>
        <div>${param.success}</div>
    </div>
</c:if>
