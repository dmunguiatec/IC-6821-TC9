import Head from 'next/head'

import Layout from '../../components/layout'
import Date from '../../components/date'
import utilStyles from '../../styles/utils.module.css'

import { getPost } from '../../lib/PostService'

export async function getServerSideProps(context) {
    const post = await getPost(context.params.extId)

    return {
        props: { post }
    }
  }
  

export default function Post({ post }) {
    return (
        <Layout>
            <Head>
                <title>{post.title}</title>
            </Head>
            <article>
                <h1 className={utilStyles.headingXl}>{post.title}</h1>
                <div className={utilStyles.lightText}>
                    <Date dateString={post.date} />
                </div>
                <div dangerouslySetInnerHTML={{ __html: post.body }} />
            </article>
        </Layout>
    )
}
