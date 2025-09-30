function copyCode() {
    const btn = event.target.closest("button")
    const icon = btn.querySelector("i")

    icon.classList.add("bounce")
    navigator.clipboard.writeText("317610").then(() => {
        setTimeout(() => {
            icon.textContent = "check"
            icon.className = "material-icons text-green-600 copy-icon success"
        }, 250)
        setTimeout(() => {
            icon.textContent = "content_copy"
            icon.className = "material-icons copy-icon"
            icon.style.color = "hsl(var(--primary))"
        }, 1500)
    })
}

function toggleStatuts() {
    const dropdown = document.getElementById("statutsDropdown")
    dropdown.classList.toggle("dropdown-open")
}

function toggleSubmitButton() {
    const checkbox = document.getElementById("confirmStatuts")
    const submitButton = document.querySelector('button[type="submit"]')

    if (submitButton) {
        if (checkbox.checked) {
            submitButton.classList.remove("submit-disabled")
            submitButton.disabled = false
        } else {
            submitButton.classList.add("submit-disabled")
            submitButton.disabled = true
        }
    }
}

function toggleMobileMenu() {
    const mobileMenu = document.getElementById("mobile-menu")
    const mobileMenuAnonymous = document.getElementById("mobile-menu-anonymous")

    if (mobileMenu) {
        mobileMenu.classList.toggle("hidden")
    }

    if (mobileMenuAnonymous) {
        mobileMenuAnonymous.classList.toggle("hidden")
    }
}

function handleResize() {
    const mobileMenu = document.getElementById("mobile-menu")
    const mobileMenuAnonymous = document.getElementById("mobile-menu-anonymous")

    if (window.innerWidth >= 768) {
        if (mobileMenu) mobileMenu.classList.add("hidden")
        if (mobileMenuAnonymous) mobileMenuAnonymous.classList.add("hidden")
    }
}

document.addEventListener("DOMContentLoaded", () => {
    const submitButton = document.querySelector('button[type="submit"]')
    if (submitButton) {
        submitButton.classList.add("submit-disabled")
        submitButton.disabled = true
    }

    const mobileMenuButton = document.getElementById("mobile-menu-button")
    if (mobileMenuButton) {
        mobileMenuButton.addEventListener("click", toggleMobileMenu)
    }

    const mobileMenuButtons = document.querySelectorAll(".md\\:hidden.focus\\:outline-none")
    mobileMenuButtons.forEach(button => {
        if (!button.id) {
            button.addEventListener("click", toggleMobileMenu)
        }
    })

    window.addEventListener("resize", handleResize)

    const dropdownToggle = document.querySelector(".dropdown-toggle")
    if (dropdownToggle) {
        dropdownToggle.addEventListener("click", (e) => {
            e.preventDefault()
            toggleStatuts()
        })
    }
})