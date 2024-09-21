<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html lang="zh-Hant">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>庫存管理系統</title>

    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">

    <!-- DataTables CSS -->
    <link rel="stylesheet" href="https://cdn.datatables.net/1.11.5/css/dataTables.bootstrap5.min.css">

    <!-- Font Awesome -->
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.10.0/css/all.min.css" rel="stylesheet">

    <!-- Google Fonts -->
    <link href="https://fonts.googleapis.com/css2?family=Heebo:wght@400;500;600;700&display=swap" rel="stylesheet">

    <!-- Custom CSS -->
    <link href="${pageContext.request.contextPath}/CSS/style.css" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/CSS/extra.css">

    <style>
        .restock-form {
            background-color: #f8f9fa;
            border-radius: 10px;
            padding: 30px;
            box-shadow: 0 0 20px rgba(0, 0, 0, 0.1);
        }
        .restock-form h2 {
            color: #0d6efd;
            margin-bottom: 30px;
            text-align: center;
        }
        .form-group {
            margin-bottom: 20px;
        }
        .form-label {
            font-weight: 600;
            color: #495057;
        }
        .form-control, .form-select {
            border-radius: 5px;
        }
        .form-control:focus, .form-select:focus {
            border-color: #0d6efd;
            box-shadow: 0 0 0 0.2rem rgba(13, 110, 253, 0.25);
        }
        .btn-submit {
            background-color: #0d6efd;
            border: none;
            padding: 10px 30px;
            font-size: 18px;
            font-weight: 600;
            margin-top: 20px;
        }
        .btn-submit:hover {
            background-color: #0b5ed7;
        }
        #errorMessage, #successMessage {
            display: none;
            margin-top: 20px;
            padding: 10px;
            border-radius: 5px;
        }
        #errorMessage {
            background-color: #f8d7da;
            color: #721c24;
            border: 1px solid #f5c6cb;
        }
        #successMessage {
            background-color: #d4edda;
            color: #155724;
            border: 1px solid #c3e6cb;
        }
    </style>
</head>
<body>
    <%@ include file="/body/body.jsp" %>
<main>
    <div class="container mt-5">
        <div class="row justify-content-center">
            <div class="col-lg-10">
                <div class="restock-form">
                    <h2>新增進貨</h2>
                    <form id="restockForm">
                        <div class="row">
                            <div class="col-md-6 form-group">
                                <label for="employee_id" class="form-label">員工編號:</label>
                                <select id="employee_id" name="employee_id" class="form-select" required>
                                    <option value="">選擇負責員工</option>
                                </select>
                            </div>
                            <div class="col-md-6 form-group">
                                <label for="restock_id" class="form-label">進貨編號:</label>
                                <input type="text" id="restock_id_display" class="form-control" readonly>
                                <input type="hidden" id="restock_id" name="restock_id">
                            </div>
                        </div>
                        
                        <div class="row">
                            <div class="col-md-6 form-group">
                                <label for="restock_date" class="form-label">進貨日期:</label>
                                <input type="date" id="restock_date" name="restock_date" class="form-control" required>
                            </div>
                            <div class="col-md-6 form-group">
                                <label for="product_category" class="form-label">商品種類:</label>
                                <select id="product_category" name="product_category" class="form-select" required>
                                    <option value="">請選擇商品種類</option>
                                </select>
                            </div>
                        </div>

                        <div class="row">
                            <div class="col-md-6 form-group">
                                <label for="product_name" class="form-label">商品名稱:</label>
                                <select id="product_name" name="product_name" class="form-select" required>
                                    <option value="">請選擇商品名稱</option>
                                </select>
                            </div>
                            <div class="col-md-6 form-group">
                                <label for="product_id" class="form-label">商品編號:</label>
                                <input type="text" id="product_id" name="product_id" class="form-control" readonly>
                            </div>
                        </div>

                        <div class="row">
                            <div class="col-md-4 form-group">
                                <label for="number_of_restock" class="form-label">進貨數量:</label>
                                <input type="number" id="number_of_restock" name="number_of_restock" class="form-control" min="1" required>
                            </div>
                            <div class="col-md-4 form-group">
                                <label for="product_price" class="form-label">商品單價:</label>
                                <input type="number" id="product_price" name="product_price" class="form-control" step="0.01" min="0" required>
                            </div>
                            <div class="col-md-4 form-group">
                                <label for="restock_total_price" class="form-label">進貨總金額:</label>
                                <input type="number" id="restock_total_price" name="restock_total_price" class="form-control" step="0.01" readonly>
                            </div>
                        </div>

                        <div class="row">
                            <div class="col-md-6 form-group">
                                <label for="production_date" class="form-label">生產日期:</label>
                                <input type="date" id="production_date" name="production_date" class="form-control" required>
                            </div>
                            <div class="col-md-6 form-group">
                                <label for="due_date" class="form-label">到期日期:</label>
                                <input type="date" id="due_date" name="due_date" class="form-control" required>
                            </div>
                        </div>

                        <div class="text-center">
                            <button type="submit" class="btn btn-primary btn-submit">新增進貨</button>
                        </div>
                    </form>
                    <div id="errorMessage"></div>
                    <div id="successMessage"></div>
                </div>
            </div>
        </div>
    </div>
</main>
    <!-- jQuery -->
    <script src="https://code.jquery.com/jquery-3.7.1.min.js"></script>

    <!-- Bootstrap JS Bundle with Popper -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>

    <!-- DataTables JS -->
    <script src="https://cdn.datatables.net/1.11.5/js/jquery.dataTables.min.js"></script>
    <script src="https://cdn.datatables.net/1.11.5/js/dataTables.bootstrap5.min.js"></script>

    <script>
    $(document).ready(function() {
        console.log("Document ready");
        const contextPath = '${pageContext.request.contextPath}';
        console.log("Context path:", contextPath);

        // 通用的 AJAX 請求函數
        function makeRequest(url, method = 'GET', data = null) {
            return $.ajax({
                url: contextPath + url,
                type: method,
                data: data,
                dataType: 'json'
            });
        }

        // 顯示錯誤消息
        function showError(message) {
            $('#errorMessage').text(message).show();
            $('#successMessage').hide();
        }

        // 顯示成功消息
        function showSuccess(message) {
            $('#successMessage').text(message).show();
            $('#errorMessage').hide();
        }

        // 獲取最新進貨 ID
        function getLatestRestockId() {
            console.log("Getting latest restock ID");
            makeRequest('/RestockServlet?action=getLatestId')
                .done(function(id) {
                    console.log("Latest restock ID received:", id);
                    $('#restock_id_display, #restock_id').val(id);
                })
                .fail(function(jqXHR, textStatus, errorThrown) {
                    console.error("Error getting latest restock ID:", textStatus, errorThrown);
                    showError('獲取進貨編號時出錯');
                });
        }


// 加載員工列表
        function loadEmployees() {
            console.log("Loading employees");
            makeRequest('/RestockServlet?action=getEmployees')
                .done(function(employees) {
                    console.log("Employees loaded:", employees);
                    var select = $('#employee_id');
                    if(select.length === 0) {
                        console.error("Employee select element not found");
                        return;
                    }
                    select.empty().append($('<option>').text('選擇負責員工').attr('value', ''));
                    $.each(employees, function(i, emp) {
                        select.append($('<option>').text(emp.employeeId + ' - ' + emp.employeeName).attr('value', emp.employeeId));
                    });
                    console.log("Employee options added:", select.find('option').length);
                })
                .fail(function(jqXHR, textStatus, errorThrown) {
                    console.error("Error loading employees:", textStatus, errorThrown);
                    showError('加載員工列表時出錯');
                });
        }

// 加載商品類別
        function loadProductCategories() {
            console.log("Loading product categories");
            makeRequest('/RestockServlet?action=getProductCategories')
                .done(function(categories) {
                    console.log("Product categories loaded:", categories);
                    var select = $('#product_category');
                    if(select.length === 0) {
                        console.error("Product category select element not found");
                        return;
                    }
                    select.empty().append($('<option>').text('請選擇商品種類').attr('value', ''));
                    $.each(categories, function(i, category) {
                        select.append($('<option>').text(category.productCategory).attr('value', category.productCategory));
                    });
                    console.log("Product category options added:", select.find('option').length);
                })
                .fail(function(jqXHR, textStatus, errorThrown) {
                    console.error("Error loading product categories:", textStatus, errorThrown);
                    showError('加載商品類別時出錯');
                });
        }

        // 根據類別加載商品名稱
        $('#product_category').change(function() {
            var category = $(this).val();
            console.log("Product category changed:", category);
            if (category) {
                makeRequest('/RestockServlet?action=getProductNames', 'GET', { category: category })
                    .done(function(products) {
                        console.log("Products loaded for category:", products);
                        var select = $('#product_name');
                        select.empty().append($('<option>').text('請選擇商品名稱').attr('value', ''));
                        $.each(products, function(i, product) {
                            select.append($('<option>')
                                .text(product.productName)
                                .attr('value', product.productId)
                                .data('productId', product.productId));
                        });
                    })
                    .fail(function(jqXHR, textStatus, errorThrown) {
                        console.error("Error loading products for category:", textStatus, errorThrown);
                        showError('獲取商品名稱時出錯');
                    });
            } else {
                $('#product_name').empty().append($('<option>').text('請選擇商品名稱').attr('value', ''));
                $('#product_id').val('');
            }
        });

        // 當選擇商品名稱時，填充商品 ID
        $('#product_name').change(function() {
            var selectedOption = $(this).find('option:selected');
            var productId = selectedOption.data('productId');
            console.log("Product selected, ID:", productId);
            $('#product_id').val(productId);
        });

        // 計算總金額
        function calculateTotal() {
            const quantity = parseFloat($('#number_of_restock').val()) || 0;
            const price = parseFloat($('#product_price').val()) || 0;
            const total = Math.round(quantity * price * 100) / 100; // 四捨五入到小數點後兩位
            console.log("Calculated total:", total);
            $('#restock_total_price').val(total.toFixed(2));
        }

        // 綁定計算總金額的事件
        $('#number_of_restock, #product_price').on('input', calculateTotal);

     // 表單提交
        $('#restockForm').submit(function(e) {
            e.preventDefault();
            console.log("Form submitted");
            if (validateForm()) {
                var formData = $(this).serialize();
                console.log("Form data:", formData);
                $.ajax({
                    url: contextPath + '/RestockServlet',
                    type: 'POST',
                    data: formData,
                    dataType: 'json',
                    success: function(response) {
                        console.log("Form submission response:", response);
                        if (response.success) {
                            showSuccess('新進貨記錄已成功創建，編號為：' + response.restockId);
                    
                            // 使用 setTimeout 來確保警告框顯示後再刷新頁面
                            setTimeout(function() {
                                window.location.reload();
                                alert('新進貨記錄已成功創建，編號為：' + response.restockId);
                            }, 10000);
                        } else {
                            // 檢查是否存在部分成功的情況
                            if (response.message && response.message.includes("已成功創建")) {
                                showSuccess(response.message);
                                
                                setTimeout(function() {
                                    window.location.reload();
                                }, 10000);
                            } else {
                                showError('處理進貨時發生錯誤: ' + (response.message || '未知錯誤'));
                            }
                        }
                    },
                    error: function(jqXHR, textStatus, errorThrown) {
                        console.error("AJAX error:", textStatus, errorThrown);
                        showError('處理進貨時發生錯誤: ' + errorThrown);
                    }
                });
            }
        });

        // 顯示錯誤消息

        

        // 顯示成功消息
  
        // 表單驗證
        function validateForm() {
            var errorMessages = [];
            if (!$('#employee_id').val()) errorMessages.push("請選擇負責員工");
            if (!$('#restock_date').val()) errorMessages.push("請選擇進貨日期");
            if (!$('#product_category').val()) errorMessages.push("請選擇商品種類");
            if (!$('#product_name').val()) errorMessages.push("請選擇商品名稱");
            if (!$('#number_of_restock').val()) errorMessages.push("請輸入進貨數量");
            if (!$('#product_price').val()) errorMessages.push("請輸入商品單價");
            if (!$('#production_date').val()) errorMessages.push("請選擇生產日期");
            if (!$('#due_date').val()) errorMessages.push("請選擇到期日期");

            if (errorMessages.length > 0) {
                showError(errorMessages.join('<br>'));
                return false;
            }
            return true;
        }

        // 初始化
        getLatestRestockId();
        loadEmployees();
        loadProductCategories();
        $('#restock_date').val(new Date().toISOString().split('T')[0]);

        // 結束 document ready 函數
        });
        </script>
        </body>
        </html>