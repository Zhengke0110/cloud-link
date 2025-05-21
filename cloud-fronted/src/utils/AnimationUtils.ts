/**
 * 初始化页面元素显示动画
 * 查找所有带有 .reveal-element 类的元素并添加进入视口时的动画
 */
export const initPageAnimations = () => {
    const revealElements = document.querySelectorAll(".reveal-element");

    if ("IntersectionObserver" in window) {
        const observer = new IntersectionObserver(
            (entries) => {
                entries.forEach((entry) => {
                    if (entry.isIntersecting) {
                        entry.target.classList.add("revealed");
                        observer.unobserve(entry.target);
                    }
                });
            },
            {
                threshold: 0.1,
            }
        );

        revealElements.forEach((el) => {
            observer.observe(el);
        });
    } else {
        // 对于不支持 IntersectionObserver 的浏览器，直接显示元素
        revealElements.forEach((el) => {
            el.classList.add("revealed");
        });
    }
};
