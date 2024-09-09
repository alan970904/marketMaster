<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>商品資料</title>
    <!-- DataTables CSS -->
    <link rel="stylesheet" type="text/css" href="https://cdn.datatables.net/1.10.24/css/jquery.dataTables.css">

    <link rel="stylesheet"
href="https://cdn.datatables.net/1.11.5/css/jquery.dataTables.min.css">
<script src="https://code.jquery.com/jquery-3.7.1.min.js"></script>
<script
 src="https://cdn.datatables.net/1.11.5/js/jquery.dataTables.min.js"></script>

<!-- Bootstrap CSS -->
<link
 href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css"
 rel="stylesheet">

<!-- DataTables CSS -->
<link rel="stylesheet"
 href="https://cdn.datatables.net/1.11.5/css/dataTables.bootstrap5.min.css">

<!-- Font Awesome -->
<link
 href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.10.0/css/all.min.css"
 rel="stylesheet">

<!-- Google Fonts -->
<link
 href="https://fonts.googleapis.com/css2?family=Heebo:wght@400;500;600;700&display=swap"
 rel="stylesheet">

<!-- Custom CSS -->
<link href="/ispan/CSS/style.css" rel="stylesheet">

<!-- jQuery -->
<script src="https://code.jquery.com/jquery-3.7.1.min.js"></script>

<!-- Bootstrap JS Bundle with Popper -->
<script
 src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>

<!-- DataTables JS -->
<script
 src="https://cdn.datatables.net/1.11.5/js/jquery.dataTables.min.js"></script>
<script
 src="https://cdn.datatables.net/1.11.5/js/dataTables.bootstrap5.min.js"></script>
 
<link rel="stylesheet" href="/ispan/CSS/extra.css">
<style>
        .inventory-low { color: red; }
        .button-disabled { opacity: 0.5; cursor: not-allowed; }
        .shelve {
            background-color: #93FF93;
        }
        .remove {
            background-color: 	#FF5151;
        }
        .update {
            background-color: #BEBEBE;
        }
        button:hover:not(:disabled) {
            color: #1a1a1a;
            background-color: #fff;
        }
        button:disabled {
            opacity: 0.5;
            cursor: not-allowed;
        }

</style>
</head>
<body>
<%@ include file="/body/body.jsp" %>
<main>
    <h2>商品資料</h2>
    <div>
        <table id="productTable" class="display">
            <thead>
                <tr>
                    <th>商品編號</th>
                    <th>商品類別名稱</th>
                    <th>商品名稱</th>
                    <th>商品售價</th>
                    <th>安全庫存量</th>
                    <th>上架數量</th>
                    <th>庫存數量</th>
                    <th>銷售數量</th>
                    <th>兌換數量</th>
                    <th>銷毀數量</th>
                    <th>下架數量</th>
                    <th>修改</th>
                    <th>上架</th>
                    <th>下架</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach items="${products}" var="product" varStatus="s">
                    <tr>
                        <td>${product.productId}</td>
                        <td>${product.productCategory}</td>
                        <td><a href="<%=request.getContextPath()%>/GetOneProduct?productId=${product.productId}">${product.productName}</a></td>
                        <td>${product.productPrice}</td>
                        <td>${product.productSafeInventory}</td>
                        <td>${product.numberOfShelve}</td>
                        <td>${product.numberOfInventory}</td>
                        <td>${product.numberOfSale}</td>
                        <td>${product.numberOfExchange}</td>
                        <td>${product.numberOfDestruction}</td>
                        <td>${product.numberOfRemove}</td>
                        <td><button class="update" onclick="location.href='<%=request.getContextPath()%>/GetUpdateProduct?productId=${product.productId}'">修改</button></td>
                        <td><button class="shelve" onclick="location.href='<%=request.getContextPath()%>/GetShelveProduct?productId=${product.productId}'">上架</button></td>
                        <td><button class="remove" onclick="location.href='<%=request.getContextPath()%>/RemoveProduct?productId=${product.productId}'">下架</button></td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
        <h3>共<span id="totalCount"></span>筆商品資料</h3>
        <input type="button" value="返回首頁" onclick="window.location.href='<%=request.getContextPath()%>/jsp/productHomepage.jsp'">
    </div>
</main>

    <!-- jQuery -->
    <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.6.0/jquery.min.js"></script>
    <!-- DataTables JS -->
    <script type="text/javascript" charset="utf8" src="https://cdn.datatables.net/1.10.24/js/jquery.dataTables.js"></script>
    <script>
        $(document).ready(function() {
            var table = $('#productTable').DataTable({
                "language": {
                    "url": "//cdn.datatables.net/plug-ins/1.10.24/i18n/Chinese-traditional.json"
                },
                "initComplete": function(settings, json) {
                    $('#productTable').on('draw.dt', function() {
                        $('#totalCount').text(table.rows().count());
                    });
                }
            });
            $('#totalCount').text(table.rows().count());
            
        });
    </script>
</body>
</html>