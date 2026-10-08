/**
 * Campus Lost & Found Portal - Client-side Form Validation
 */

document.addEventListener('DOMContentLoaded', function () {
    
    // Auto-attach validation to forms with class 'needs-validation'
    const forms = document.querySelectorAll('.needs-validation');
    
    forms.forEach(function (form) {
        form.addEventListener('submit', function (event) {
            let isValid = true;
            
            // Validate text fields with data-rules
            const inputs = form.querySelectorAll('input, select, textarea');
            inputs.forEach(function (input) {
                if (!validateInput(input)) {
                    isValid = false;
                }
            });

            // Password confirmation check if present
            const pass = form.querySelector('#password');
            const confirmPass = form.querySelector('#confirmPassword');
            if (pass && confirmPass && confirmPass.value) {
                if (pass.value !== confirmPass.value) {
                    showError(confirmPass, 'Passwords do not match');
                    isValid = false;
                }
            }

            if (!isValid) {
                event.preventDefault();
                event.stopPropagation();
            }
        });

        // Real-time validation on input/blur
        form.querySelectorAll('input, select, textarea').forEach(function (input) {
            input.addEventListener('blur', function () {
                validateInput(input);
            });
            input.addEventListener('input', function () {
                if (input.classList.contains('is-invalid')) {
                    validateInput(input);
                }
            });
        });
    });
});

function validateInput(input) {
    const value = input.value ? input.value.trim() : '';
    const name = input.getAttribute('name') || input.id;
    let valid = true;
    let errorMessage = '';

    // Check required
    if (input.hasAttribute('required') && value === '') {
        showError(input, 'This field is required.');
        return false;
    }

    // Name Validation
    if (name === 'name' && value.length > 0) {
        if (value.length < 2 || value.length > 100) {
            valid = false;
            errorMessage = 'Full name must be 2-100 characters long.';
        }
    }

    // Username Validation
    if (name === 'username' && value.length > 0) {
        const userRegex = /^[a-zA-Z0-9_]{3,50}$/;
        if (!userRegex.test(value)) {
            valid = false;
            errorMessage = 'Username must be 3-50 letters, numbers, or underscores.';
        }
    }

    // Email Validation
    if ((name === 'email' || input.type === 'email') && value.length > 0) {
        const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        if (!emailRegex.test(value)) {
            valid = false;
            errorMessage = 'Please enter a valid email address.';
        }
    }

    // Password Validation
    if (name === 'password' && value.length > 0) {
        if (value.length < 8) {
            valid = false;
            errorMessage = 'Password must be at least 8 characters long.';
        } else if (!/\d/.test(value)) {
            valid = false;
            errorMessage = 'Password must contain at least one number.';
        }
    }

    // Phone Validation
    if (name === 'phone' && value.length > 0) {
        const phoneRegex = /^[0-9]{10,15}$/;
        if (!phoneRegex.test(value)) {
            valid = false;
            errorMessage = 'Phone number must be 10-15 digits.';
        }
    }

    // Item Name
    if (name === 'itemName' && value.length > 0) {
        if (value.length < 2 || value.length > 100) {
            valid = false;
            errorMessage = 'Item name must be between 2 and 100 characters.';
        }
    }

    // Category
    if (name === 'categoryId' && input.tagName === 'SELECT') {
        if (!value || value === '0') {
            valid = false;
            errorMessage = 'Please select a valid category.';
        }
    }

    // Description
    if (name === 'description' && value.length > 0) {
        if (value.length < 5 || value.length > 500) {
            valid = false;
            errorMessage = 'Description must be between 5 and 500 characters.';
        }
    }

    // Location
    if (name === 'location' && value.length > 0) {
        if (value.length < 2 || value.length > 150) {
            valid = false;
            errorMessage = 'Location must be between 2 and 150 characters.';
        }
    }

    // Date
    if (name === 'incidentDate' && value.length > 0) {
        const selectedDate = new Date(value);
        const today = new Date();
        today.setHours(23, 59, 59, 999);
        if (selectedDate > today) {
            valid = false;
            errorMessage = 'Incident date cannot be in the future.';
        }
    }

    // Proof description
    if (name === 'proofDescription' && value.length > 0) {
        if (value.length < 10 || value.length > 500) {
            valid = false;
            errorMessage = 'Proof description must be at least 10 characters long.';
        }
    }

    if (valid) {
        clearError(input);
    } else {
        showError(input, errorMessage);
    }

    return valid;
}

function showError(input, message) {
    input.classList.add('is-invalid');
    let feedback = input.nextElementSibling;
    if (!feedback || !feedback.classList.contains('invalid-feedback')) {
        feedback = document.createElement('div');
        feedback.className = 'invalid-feedback';
        input.parentNode.appendChild(feedback);
    }
    feedback.textContent = message;
    feedback.style.display = 'block';
}

function clearError(input) {
    input.classList.remove('is-invalid');
    const feedback = input.parentNode.querySelector('.invalid-feedback');
    if (feedback) {
        feedback.style.display = 'none';
    }
}
