// Adding an organization contact
function addOrgContact(button) {

  const table = button.closest('.collapse-content').querySelector('tbody');
  if (!table) {
    console.error('tbody not found');
    return;
  }

  let row = document.createElement('tr');

  row.innerHTML = `
            <td>
                <input type="hidden" name="contact_id[]" value="">
                <input type="text" name="org_contact_name[]">
            </td>
            <td><input type="text" name="org_contact_position[]"></td>
            <td><input type="text" name="org_contact_phone[]"></td>
            <td><input type="text" name="org_contact_address[]"></td>
            <td class="delete-row" onclick="deleteTableRow(this)">✖</td>
        `;

  table.appendChild(row);
}

// Initialization for the organization page
document.addEventListener('DOMContentLoaded', function() {
  // Organization-specific logic
  resizeAllTextareas();
});