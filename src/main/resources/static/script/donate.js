function copyCode() {
    const button = event.target.closest("button")
    const iconElement = button.querySelector("i")

    iconElement.classList.add("bounce")
    navigator.clipboard.writeText("317610").then(() => {
        setTimeout(() => {
            iconElement.textContent = "check"
            iconElement.className = "material-icons text-green-600 copy-icon success"
        }, 250)
        setTimeout(() => {
            iconElement.textContent = "content_copy"
            iconElement.className = "material-icons copy-icon"
            iconElement.style.color = "hsl(var(--primary))"
        }, 1500)
    })
}

function toggleStatuts() {
    const dropdownElement = document.getElementById("statutsDropdown")
    dropdownElement.classList.toggle("dropdown-open")
}

function toggleSubmitButton() {
    const checkboxElement = document.getElementById("confirmStatuts")
    const submitButtonElement = document.querySelector('button[type="submit"]')

    if (submitButtonElement) {
        if (checkboxElement.checked) {
            submitButtonElement.classList.remove("submit-disabled")
            submitButtonElement.disabled = false
        } else {
            submitButtonElement.classList.add("submit-disabled")
            submitButtonElement.disabled = true
        }
    }
}

function toggleMobileMenu() {
    const mobileMenuElement = document.getElementById("mobile-menu")
    const mobileMenuAnonymousElement = document.getElementById("mobile-menu-anonymous")

    if (mobileMenuElement) {
        mobileMenuElement.classList.toggle("hidden")
    }

    if (mobileMenuAnonymousElement) {
        mobileMenuAnonymousElement.classList.toggle("hidden")
    }
}

function handleResize() {
    const mobileMenuElement = document.getElementById("mobile-menu")
    const mobileMenuAnonymousElement = document.getElementById("mobile-menu-anonymous")

    if (window.innerWidth >= 768) {
        if (mobileMenuElement) {
            mobileMenuElement.classList.add("hidden")
        }
        if (mobileMenuAnonymousElement) {
            mobileMenuAnonymousElement.classList.add("hidden")
        }
    }
}

document.addEventListener("DOMContentLoaded", () => {
    const submitButtonElement = document.querySelector('button[type="submit"]')
    if (submitButtonElement) {
        submitButtonElement.classList.add("submit-disabled")
        submitButtonElement.disabled = true
    }

    const mobileMenuButtons = document.querySelectorAll("header button.md\\:hidden")
    mobileMenuButtons.forEach(buttonElement => {
        buttonElement.addEventListener("click", toggleMobileMenu)
    })

    window.addEventListener("resize", handleResize)

    const dropdownToggleElement = document.querySelector(".dropdown-toggle")
    if (dropdownToggleElement) {
        dropdownToggleElement.addEventListener("click", (event) => {
            event.preventDefault()
            toggleStatuts()
        })
    }
})