
function copyCode() {
    const btn = event.target.closest('button');
    const icon = btn.querySelector('i');

    icon.classList.add('bounce');

    navigator.clipboard.writeText("317610").then(() => {
        setTimeout(() => {
            icon.textContent = 'check';
            icon.className = 'material-icons text-green-600 copy-icon success';
        }, 250);

        setTimeout(() => {
            icon.textContent = 'content_copy';
            icon.className = 'material-icons copy-icon primary-color';
        }, 1500);
    }).catch(err => {
        console.error('Erreur lors de la copie:', err);
        // Fallback pour les navigateurs qui ne supportent pas l'API clipboard
        fallbackCopyTextToClipboard("317610");
    });
}

function fallbackCopyTextToClipboard(text) {
    const textArea = document.createElement("textarea");
    textArea.value = text;
    textArea.style.top = "0";
    textArea.style.left = "0";
    textArea.style.position = "fixed";

    document.body.appendChild(textArea);
    textArea.focus();
    textArea.select();

    try {
        const successful = document.execCommand('copy');
        if (successful) {
            console.log('Fallback: Copie réussie');
        }
    } catch (err) {
        console.error('Fallback: Impossible de copier', err);
    }

    document.body.removeChild(textArea);
}

function toggleStatuts() {
    const dropdown = document.getElementById('statutsDropdown');
    dropdown.classList.toggle('dropdown-open');
}

function toggleSubmitButton() {
    const checkbox = document.getElementById('confirmStatuts');
    const submitButton = document.querySelector('button[type="submit"]');

    if (submitButton) {
        if (checkbox.checked) {
            submitButton.classList.remove('submit-disabled');
            submitButton.disabled = false;
        } else {
            submitButton.classList.add('submit-disabled');
            submitButton.disabled = true;
        }
    }
}

function validateForm() {
    const pspId = document.getElementById('pspId').value.trim();
    const firstName = document.getElementById('firstName').value.trim();
    const lastName = document.getElementById('lastName').value.trim();
    const confirmStatuts = document.getElementById('confirmStatuts').checked;

    if (!pspId) {
        alert('Veuillez saisir la référence de paiement Orange Money');
        return false;
    }

    if (!firstName) {
        alert('Veuillez saisir votre prénom');
        return false;
    }

    if (!lastName) {
        alert('Veuillez saisir votre nom');
        return false;
    }

    if (!confirmStatuts) {
        alert('Vous devez accepter les statuts constitutifs pour continuer');
        return false;
    }

    return true;
}

document.addEventListener('DOMContentLoaded', function() {
    const submitButton = document.querySelector('button[type="submit"]');
    if (submitButton) {
        submitButton.classList.add('submit-disabled');
        submitButton.disabled = true;
    }

    const form = document.querySelector('form');
    if (form) {
        form.addEventListener('submit', function(e) {
            if (!validateForm()) {
                e.preventDefault();
                return false;
            }
        });
    }

    document.addEventListener('click', function(event) {
        const dropdown = document.getElementById('statutsDropdown');
        if (dropdown && !dropdown.contains(event.target)) {
            dropdown.classList.remove('dropdown-open');
        }
    });

    const dropdownToggle = document.querySelector('.dropdown-toggle');
    if (dropdownToggle) {
        dropdownToggle.addEventListener('keydown', function(e) {
            if (e.key === 'Enter' || e.key === ' ') {
                e.preventDefault();
                toggleStatuts();
            }
        });
    }

    const statutsCheckbox = document.getElementById('confirmStatuts');
    if (statutsCheckbox) {
        statutsCheckbox.addEventListener('keydown', function(e) {
            if (e.key === 'Enter') {
                e.preventDefault();
                this.checked = !this.checked;
                toggleSubmitButton();
            }
        });
    }
});