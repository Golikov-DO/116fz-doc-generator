// Добавление контакта организации
function addOrgContact(button) {
  const tbody = button.closest('.collapse-content').querySelector('.org-contacts-body');
  const newRow = document.createElement('tr');
  newRow.innerHTML = `
        <td><input type="text" name="org_contact_name[]" placeholder="ФИО" style="width: 100%;"></td>
        <td><input type="text" name="org_contact_position[]" placeholder="Должность" style="width: 100%;"></td>
        <td><input type="text" name="org_contact_phone[]" placeholder="Телефон" style="width: 100%;"></td>
        <td><input type="text" name="org_contact_address[]" placeholder="Адрес" style="width: 100%;"></td>
        <td class="delete-row" onclick="deleteRow(this)">✖</td>
    `;
  tbody.appendChild(newRow);
}

// Инициализация для страницы организации (если нужно)
document.addEventListener('DOMContentLoaded', function() {
  // Специфичная для организации логика
  console.log('Organization fragment loaded');
});