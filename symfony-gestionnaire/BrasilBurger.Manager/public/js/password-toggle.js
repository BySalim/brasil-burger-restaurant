/**
 * Password Toggle Visibility
 * Toggles password input between text and password type
 */
function togglePassword(inputId) {
    const input = document.getElementById(inputId);
    const icon = document.getElementById(inputId + '_icon');

    if (!input || !icon) {
        console.error('Password input or icon not found:', inputId);
        return;
    }

    // Toggle input type
    if (input.type === 'password') {
        input.type = 'text';
        icon.textContent = 'visibility_off';
    } else {
        input.type = 'password';
        icon.textContent = 'visibility';
    }
}
