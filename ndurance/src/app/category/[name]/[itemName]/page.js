import Navigation from "@/app/navigation";
import SHOP_DATA from '@/shopData';

const ProductPage = ({ params }) => {
    const { name, itemName } = params;

    const category = SHOP_DATA.find(category => category.name.toLowerCase() === name.toLowerCase());
    if (!category) {
        return <div>Category not found</div>;
    }

    const product = category.items.find(
        item => item.name.toLowerCase().replace(/ /g, '-') === itemName
    );

    if (!product) {
        return <div>Product not found</div>;
    }

    return (
        <div>
            <Navigation/>
            <h1>{product.name}</h1>
            <img src={product.imageUrl} alt={product.name} />
            <p>Price: ${product.price}</p>
        </div>
    );
};

export default ProductPage;
