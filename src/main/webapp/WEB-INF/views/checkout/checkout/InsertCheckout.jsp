<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="java.util.List, com.MarketMaster.bean.employee.EmpBean,com.MarketMaster.bean.product.ProductBean, com.MarketMaster.bean.checkout.CheckoutBean,com.MarketMaster.bean.checkout.CheckoutDetailsBean" %>
<% if (request.getAttribute("employees") == null) {
    response.sendRedirect(request.getContextPath() + "/CheckoutServlet");
    return;}%>
<!DOCTYPE html>
<html lang="zh-Hant">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>新增結帳記錄</title>
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
<link href="${pageContext.request.contextPath}/CSS/style.css" rel="stylesheet">

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

<link rel="stylesheet" href="${pageContext.request.contextPath}/CSS/extra.css">
<style>
main{
/* 全局样式 */
body {
    background-color: #f0f2f5;
    font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
    color: #333;
    line-height: 1.6;
}

.container {
    max-width: 1200px;
    margin: 0 auto;
    padding: 20px;
}

/* 卡片样式 */
.card {
    background-color: #ffffff;
    border-radius: 15px;
    box-shadow: 0 10px 30px rgba(0, 0, 0, 0.1);
    padding: 2.5rem;
    margin: 2rem auto;
    max-width: 800px;
}

.card h2 {
    color: #2c3e50;
    margin-bottom: 2rem;
    text-align: center;
    font-size: 2.2rem;
    font-weight: 600;
    text-transform: uppercase;
    letter-spacing: 1px;
}

/* 表单网格布局 */
.form-grid {
    display: grid;
    grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
    gap: 2rem;
}

/* 表单组样式 */
.form-group {
    margin-bottom: 1.5rem;
}

.form-group label {
    display: block;
    margin-bottom: 0.7rem;
    color: #34495e;
    font-weight: 500;
    font-size: 1.1rem;
}

.form-control {
    width: 100%;
    padding: 0.8rem 1rem;
    border: 2px solid #e0e0e0;
    border-radius: 8px;
    font-size: 1rem;
    transition: border-color 0.3s ease, box-shadow 0.3s ease;
}

.form-control:focus {
    outline: none;
    border-color: #3498db;
    box-shadow: 0 0 0 3px rgba(52, 152, 219, 0.2);
}

/* 电话号码输入框样式 */
.phone-group .phone-inputs {
    display: flex;
    align-items: center;
}

.phone-group .form-control {
    flex: 1;
}

.phone-separator {
    margin: 0 10px;
    color: #34495e;
    font-weight: bold;
}

/* 下拉菜单样式 */
select.form-control {
    appearance: none;
    background-image: url("data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' width='12' height='12' fill='%2334495e' viewBox='0 0 16 16'%3E%3Cpath d='M7.247 11.14L2.451 5.658C1.885 5.013 2.345 4 3.204 4h9.592a1 1 0 0 1 .753 1.659l-4.796 5.48a1 1 0 0 1-1.506 0z'/%3E%3C/svg%3E");
    background-repeat: no-repeat;
    background-position: right 1rem center;
    background-size: 12px;
    padding-right: 2.5rem;
}

/* 按钮样式 */
.btn {
    background-color: #3498db;
    color: white;
    padding: 0.8rem 1.5rem;
    border: none;
    border-radius: 8px;
    cursor: pointer;
    font-size: 1.1rem;
    font-weight: 500;
    transition: all 0.3s ease;
    text-transform: uppercase;
    letter-spacing: 1px;
}

.btn:hover {
    background-color: #2980b9;
    transform: translateY(-2px);
    box-shadow: 0 4px 10px rgba(52, 152, 219, 0.3);
}

#addProduct {
    margin-top: 1.5rem;
    margin-bottom: 1.5rem;
    background-color: #2ecc71;
}

#addProduct:hover {
    background-color: #27ae60;
}

/* 产品列表样式 */
.product-list {
    margin-top: 1.5rem;
    border: 2px solid #e0e0e0;
    border-radius: 8px;
    padding: 1.5rem;
    background-color: #f9f9f9;
}

.product-list p {
    margin-bottom: 1rem;
    padding: 1rem;
    background-color: #fff;
    border: 1px solid #e0e0e0;
    border-radius: 8px;
    display: flex;
    justify-content: space-between;
    align-items: center;
    transition: all 0.3s ease;
}

.product-list p:hover {
    transform: translateY(-2px);
    box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);
}

.removeProduct {
    background-color: #e74c3c;
    color: white;
    border: none;
    border-radius: 6px;
    padding: 0.5rem 0.8rem;
    cursor: pointer;
    font-size: 0.9rem;
    transition: all 0.3s ease;
}

.removeProduct:hover {
    background-color: #c0392b;
    transform: translateY(-1px);
}

/* 错误消息样式（已移除） */
.error-message {
    display: none;
    height: 0;
    margin: 0;
    padding: 0;
    border: none;
}

/* 返回按钮样式 */
#back {
    background-color: #95a5a6;
    color: white;
    padding: 0.8rem 1.5rem;
    border: none;
    border-radius: 8px;
    cursor: pointer;
    font-size: 1.1rem;
    transition: all 0.3s ease;
    text-transform: uppercase;
    letter-spacing: 1px;
    margin-top: 1.5rem;
    margin-bottom: 0; /* 确保底部没有额外的空间 */
}

#back:hover {
    background-color: #7f8c8d;
    transform: translateY(-2px);
    box-shadow: 0 4px 10px rgba(149, 165, 166, 0.3);
}

/* 响应式设计 */
@media (max-width: 768px) {
    .form-grid {
        grid-template-columns: 1fr;
    }
    
    .card {
        padding: 1.5rem;
    }
    
    .btn, #back {
        width: 100%;
    }
}

}
</style>
</head>
<body>
    <%@ include file="../../body/body.jsp"%>
    <main>
   
        <section id="checkout" class="section active">
            <c:if test="${not empty errorMessage}">
                <div class="error-message">${errorMessage}</div>
            </c:if>
            <div class="card">
                <h2>新增結帳記錄</h2>
                <form id="checkoutForm" action="${pageContext.request.contextPath}/CheckoutServlet" method="post" onsubmit="return validateForm()">
                    <input type="hidden" name="action" value="insert">
                    <div class="form-grid">
                        <div class="form-group">
                            <label for="checkoutId">結帳編號:</label>
                            <input type="text" id="checkoutId" name="checkoutId" class="form-control" style="width: 85%" readonly>
                        </div>
                        <div class="form-group phone-group">
                <label for="customerTel">顧客手機:</label>
                <div class="phone-inputs">
                    <input type="text" id="customerTel1" name="customerTel1" class="form-control" maxlength="4">
                    <span class="phone-separator">-</span>
                    <input type="text" id="customerTel2" name="customerTel2" class="form-control" maxlength="6">
                </div>
            </div>
                        <div class="form-group">
                            <label for="employeeId">員工編號:</label>
                            <select id="employeeId" name="employeeId" class="form-control" required>
                                <option value="">請選擇員工</option>
                                <c:forEach var="emp" items="${employees}">
                                    <option value="${emp.employeeId}">${emp.employeeId} - ${emp.employeeName}</option>
                                </c:forEach>
                            </select>
                        </div>
                        <div class="form-group">
                            <label for="productCategory">產品類別:</label>
                            <select id="product_category" name="productCategory" class="form-control" >
                                <option value="">請選擇產品類別</option>
                                <option value="肉品海鮮">肉品海鮮</option>
                                <option value="飲品">飲品</option>
                                <option value="米飯麵條">米飯麵條</option>
                                <option value="零食點心">零食點心</option>
                                <option value="蔬菜水果">蔬菜水果</option>
                            </select>
                        </div>
                        <div class="form-group">
                            <label for="product_name">商品名稱:</label>
                            <select id="product_name" name="product_name" class="form-control">
                                <option value="">請選擇商品名稱</option>
                            </select>
                        </div>
                        <div class="form-group">
                            <label for="productId">商品編號:</label>
                            <input type="text" id="product_id" name="productId" class="form-control" readonly>
                        </div>
                        <div class="form-group">
                            <label for="quantity">商品數量:</label>
                            <input type="number" id="quantity" name="quantity" class="form-control" min="1" style="width: 85%;">
                        </div>
                        <div class="form-group">
                            <label for="productPrice">商品價格:</label>
                            <input type="number" id="product_price" name="productPrice" class="form-control" readonly>
                        </div>
                        <div class="form-group">
                            <label for="subtotal">小計:</label>
                            <input type="number" id="subtotal" name="subtotal" class="form-control" readonly>
                        </div>
                    </div>
                    <button type="button" id="addProduct" class="btn">添加商品</button>
                    <div id="productList" class="product-list"></div>
                    <div class="form-group">
                        <label for="totalAmount">總結帳金額:</label>
                        <input type="number" id="totalAmount" name="totalAmount" class="form-control" readonly>
                    </div>
                    <div class="form-group">
                        <label for="checkoutDate">結帳日期:</label>
                        <input type="date" id="checkoutDate" name="checkoutDate" class="form-control" >
                    </div>
                    <div class="form-group">
                        <label for="bonusPoints">紅利點數:</label>
                        <input type="number" id="bonusPoints" name="bonusPoints" class="form-control" readonly>
                    </div>
                    <div class="form-group">
    					<label for="pointsDueDateDisplay">紅利點數到期日:</label>
    					<input type="text" id="pointsDueDateDisplay" class="form-control" placeholder="yyyy/mm/dd" readonly>
    					<input type="date" id="pointsDueDate" name="pointsDueDate" class="form-control" style="display: none;" readonly>
					</div>
                    <button type="submit" class="btn">新增結帳記錄</button>
                </form>
                <br>
                <button id="back" class="action-button">返回主頁</button>
                <div id="errorMessages" class="error-message"></div>
            </div>
        </section>
   
    </main>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>
    <script>
    $(document).ready(function() {
        // 自動生成結帳編號
        $.get("${pageContext.request.contextPath}/CheckoutServlet?action=getNextCheckoutId", function(data) {
            $("#checkoutId").val(data);
        });
      
        // 產品類別變更時獲取對應的商品
        $('#product_category').change(function() {
            var category = $(this).val();
            if (category) {
                $.ajax({
                    url: '${pageContext.request.contextPath}/CheckoutServlet',
                    type: 'GET',
                    data: {
                        action: 'getProductNames',
                        category: category
                    },
                    dataType: 'json',
                    success: function(data) {
                        var productSelect = $('#product_name');
                        productSelect.empty();
                        productSelect.append($('<option>').text('請選擇商品名稱').attr('value', ''));
                        $.each(data, function(i, product) {
                            productSelect.append($('<option>')
                                .text(product.productName)
                                .attr('value', product.productId)
                                .data('productId', product.productId)
                                .data('productPrice', product.productPrice));
                        });
                    },
                    error: function(jqXHR, textStatus, errorThrown) {
                        console.error('獲取商品名稱失敗:', textStatus, errorThrown);
                        alert('獲取商品名稱時出錯，請稍後再試');
                    }
                });
            } else {
                $('#product_name').empty().append($('<option>').text('請選擇商品名稱').attr('value', ''));
                $('#product_id').val('');
                $('#product_price').val('');
            }
        });
         
        // 商品名稱變更時更新商品編號和價格
        $('#product_name').change(function() {
            var selectedOption = $(this).find('option:selected');
            var productId = selectedOption.val();
            var productPrice = selectedOption.data('productPrice');  
            $('#product_id').val(productId);
            $('#product_price').val(productPrice);
            calculateSubtotal();
        });
            
        // 設置當前日期
        var today = new Date().toISOString().split('T')[0];
        $("#checkoutDate").val(today);
            
        // 顧客手機號碼自動跳轉
        $("#customerTel1").on('input', function() {
            if (this.value.length === 4) {
                $("#customerTel2").focus();
            }
        });
            
        // 計算小計
        function calculateSubtotal() {
            var quantity = $("#quantity").val();
            var price = $("#product_price").val();
            var subtotal = quantity * price;
            $("#subtotal").val(subtotal.toFixed(2));
        }
        $("#quantity").on('input', calculateSubtotal);
            
        // 添加商品按鈕點擊事件
        $("#addProduct").click(function() {
    var productName = $("#product_name option:selected").text();
    var productId = $("#product_id").val();
    var quantity = $("#quantity").val();
    var price = $("#product_price").val();
    var subtotal = (parseFloat(quantity) * parseFloat(price)).toFixed(2);

    if (productName && quantity && price) {
        var productHtml = '<p data-product-id="' + productId + '">' +
            productName + ' - 數量: <span class="quantity">' + quantity + '</span> x ' +
            '價格: $<span class="price">' + price + '</span> = ' +
            '小計: $<span class="subtotal">' + subtotal + '</span>' +
            '<button class="removeProduct">刪除</button></p>';
        $("#productList").append(productHtml);
        calculateTotal();
        resetProductInputs();
    } else {
        alert('請填寫所有商品信息');
    }
});

        // 刪除商品事件（使用事件委託）
        $("#productList").on('click', '.removeProduct', function() {
            $(this).parent().remove();
            calculateTotal();
        });
            
        // 計算總金額
        function calculateTotal() {
            var total = 0;
            $("#productList p").each(function() {
                var subtotal = parseFloat($(this).find('.subtotal').text());
                total += subtotal;
            });
            $("#totalAmount").val(total.toFixed(2));
            calculateBonusPoints(total);
        }
         
        // 重置商品輸入欄位
        function resetProductInputs() {
            $("#product_category").val("");
            $("#product_name").empty().append($('<option>').text('請選擇商品名稱').attr('value', ''));
            $("#product_id").val("");
            $("#quantity").val("");
            $("#product_price").val("");
            $("#subtotal").val("");
        }
        

        // 设置默认显示文本
        $("#pointsDueDateDisplay").val("yyyy/mm/dd");
         
     // 在计算红利点数的函数中更新显示
        function calculateBonusPoints(total) {
            var points = Math.floor(total / 100);
            $("#bonusPoints").val(points);
            
            if ($("#customerTel1").val() && $("#customerTel2").val()) {
                var dueDate = new Date();
                dueDate.setFullYear(dueDate.getFullYear() + 1);
                var formattedDate = dueDate.getFullYear() + '/' + 
                                    ('0' + (dueDate.getMonth() + 1)).slice(-2) + '/' + 
                                    ('0' + dueDate.getDate()).slice(-2);
                $("#pointsDueDate").val(dueDate.toISOString().split('T')[0]);
                $("#pointsDueDateDisplay").val(formattedDate);
            } else {
                $("#bonusPoints").val(0);
                $("#pointsDueDate").val("");
                $("#pointsDueDateDisplay").val("yyyy/mm/dd");
            }
        }
            
        // 表單提交事件
        $('#checkoutForm').submit(function(e) {
            e.preventDefault();
            
            var products = [];
            $('#productList p').each(function() {
                var $product = $(this);
                var productId = $product.data('product-id');
                var quantity = parseInt($product.find('.quantity').text());
                var price = parseFloat($product.find('.price').text());
                var subtotal = parseFloat($product.find('.subtotal').text());
                
                products.push({
                    productId: productId,
                    quantity: quantity,
                    price: price,
                    subtotal: subtotal
                });
            });

            console.log('Products to be sent:', products); // 添加這行來調試

            if (products.length === 0) {
                alert('請至少添加一件商品');
                return;
            }

            var formData = {
                checkoutId: $('#checkoutId').val(),
                customerTel: $('#customerTel1').val() + $('#customerTel2').val(),
                employeeId: $('#employeeId').val(),
                totalAmount: $('#totalAmount').val(),
                checkoutDate: $('#checkoutDate').val(),
                bonusPoints: $('#bonusPoints').val(),
                pointsDueDate: $('#pointsDueDate').val(),
                products: JSON.stringify(products)
            };

            console.log('Form data to be sent:', formData); // 添加這行來調試

            $.ajax({
                url: '${pageContext.request.contextPath}/CheckoutServlet?action=insertCheckoutWithDetails',
                type: 'POST',
                data: formData,
                success: function(response) {
                    if (response.status === 'success') {
                        alert('結帳記錄新增成功');
                        // 在用戶點擊確定後跳轉到所有結帳紀錄頁面
                        window.location.href = "${pageContext.request.contextPath}/CheckoutServlet?action=getAll";
                    } else {
                        alert('結帳記錄新增失敗: ' + response.message);
                    }
                },
                error: function(xhr, status, error) {
                    console.error('AJAX error:', status, error);
                    alert('發生錯誤，請稍後再試');
                }
            });
        });
        
        
        
    });

    function validateForm() {
        var errorMessages = [];
        var errorMessageDiv = document.getElementById('errorMessages');
        errorMessageDiv.innerHTML = '';

        if (!$('#employeeId').val()) {
            errorMessages.push("請選擇員工");
        }

        if ($('#productList').children().length === 0) {
            errorMessages.push("請至少添加一件商品");
        }

        if (!$('#customerTel1').val() || !$('#customerTel2').val()) {
            errorMessages.push("請輸入完整的顧客手機號碼");
        }

        if (errorMessages.length > 0) {
            errorMessageDiv.innerHTML = errorMessages.join('<br>');
            return false;
        }

        return true;
    }

    document.getElementById('back').addEventListener('click', function() {
        window.location.href = "${pageContext.request.contextPath}/checkout/checkout/index.jsp";
    });
    </script>
</body>
</html>