import Head from 'next/head'
import Link from 'next/link'

import Layout, { siteTitle } from '../components/layout'
import Date from '../components/date'
import utilStyles from '../styles/utils.module.css'

import { getPosts } from '../lib/PostService'

export async function getServerSideProps(context) {
  const posts = await getPosts()

  return {
      props: { posts }
  }
}

export default function Home({ posts }) {
  return (
    <Layout home>
      <Head>
        <title>{siteTitle}</title>
      </Head>
      <section className={utilStyles.headingMd}>
        <p>Aplicación de blogging de ejemplo para el curso IC-6821</p>
      </section>

      <section className={`${utilStyles.headingMd} ${utilStyles.padding1px}`}>
        <h2 className={utilStyles.headingLg}>Artículos</h2>
        <ul className={utilStyles.list}>
          {posts.map(({ extId, date, title }) => (
            <li className={utilStyles.listItem} key={extId}>
              <Link href={`/posts/${extId}`}>
                <a>{title}</a>
              </Link>
              <br />
              <small className={utilStyles.lightText}>
                <Date dateString={date} />
              </small>
            </li>
          ))}
        </ul>
      </section>
    </Layout>
  )
}
